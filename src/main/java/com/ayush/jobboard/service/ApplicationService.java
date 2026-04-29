package com.ayush.jobboard.service;

import com.ayush.jobboard.dto.Application.ApplicationDetailResponseDto;
import com.ayush.jobboard.dto.Application.ApplicationResponseDto;
import com.ayush.jobboard.dto.Application.ApplicationUpdateRequestDto;
import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.entity.User;

import com.ayush.jobboard.event.StatusUpdatedEvent;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.exceptions.UnAuthorizedException;
import com.ayush.jobboard.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ayush.jobboard.util.AppUtils.getCurrentUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationEventPublisher eventPublisher;
    public Page<ApplicationResponseDto> getMyApplications(Pageable pageable) {
        User applicant = getCurrentUser();
        Page<Application> applications = applicationRepository.findByApplicantId(applicant.getId() , pageable);

       return applications.map((application) -> ApplicationResponseDto.builder()
                .applicationId(application.getId())
                .jobId(application.getJob().getId())
                .appliedAt(application.getCreatedAt())
                .jobTitle(application.getJob().getTitle())
                .status(application.getStatus())
                .company(application.getJob().getCompanyName())
                .location(application.getJob().getLocation())
                .build());
    }

    public void withdrawApplication(UUID applicationId) {
        User applicant = getCurrentUser();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id '" + applicationId + "' not found"));

        if(!applicant.getId().equals(application.getApplicant().getId())){
            throw new AccessDeniedException( "You are not authorized to withdraw this application");
        }

        applicationRepository.delete(application);

    }

    public void updateApplicationStatus(UUID applicationId , ApplicationUpdateRequestDto requestDto) {

        User recruiter = getCurrentUser();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id '" + applicationId + "' not found"));

        if(!recruiter.getId().equals(application.getJob().getRecruiter().getId())){
            throw new AccessDeniedException( "You are not authorized to update the status of this application");
        }

        application.setStatus(requestDto.getStatus());
        applicationRepository.save(application);

        eventPublisher.publishEvent(new StatusUpdatedEvent(this, application, requestDto.getStatus()));
    }

    public ApplicationDetailResponseDto getApplicationDetails(UUID applicationId) {

        User user = getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application with id '" + applicationId + "' not found"));

        boolean isRecruiter = application.getJob().getRecruiter().getId().equals(user.getId());
        boolean isApplicant = application.getApplicant().getId().equals(user.getId());

        if (!isRecruiter && !isApplicant) {
            throw new AccessDeniedException("You are not authorized to view this application");
        }

        return ApplicationDetailResponseDto.builder()
                .applicationId(application.getId())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .company(application.getJob().getCompanyName())
                .location(application.getJob().getLocation())
                .applicantId(application.getApplicant().getId())
                .applicantName(application.getApplicant().getName())
                .applicantEmail(application.getApplicant().getEmail())
                .resumeUrl(application.getResumeUrl())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .appliedAt(application.getCreatedAt())
                .build();
    }
}
