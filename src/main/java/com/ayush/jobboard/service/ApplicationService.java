package com.ayush.jobboard.service;

import com.ayush.jobboard.dto.Application.ApplicationResponseDto;
import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.exceptions.AccessDeniedException;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ayush.jobboard.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    public Page<ApplicationResponseDto> getMyApplications(Pageable pageable) {
        User applicant = getCurrentUser();
        Page<Application> applications = applicationRepository.findByApplicantId(applicant.getId() , pageable);

       return applications.map((application) -> ApplicationResponseDto.builder()
                .applicationId(application.getId())
                .jobId(application.getJob().getId())
                .appliedAt(application.getCreatedAt())
                .jobTitle(application.getJob().getTitle())
                .status(application.getStatus())
                .company(application.getJob().getCompany())
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
}
