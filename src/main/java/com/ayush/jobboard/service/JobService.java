package com.ayush.jobboard.service;


import com.ayush.jobboard.dto.Application.ApplicantResponseDto;
import com.ayush.jobboard.dto.Job.JobApplyRequestDto;
import com.ayush.jobboard.dto.Job.JobFilterDto;
import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.SavedJob;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.ApplicationStatus;
import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.exceptions.*;
import com.ayush.jobboard.mapper.JobMapper;
import com.ayush.jobboard.repository.ApplicationRepository;
import com.ayush.jobboard.repository.JobRepository;
import com.ayush.jobboard.repository.SavedJobRespository;
import com.ayush.jobboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

import java.util.UUID;


import static com.ayush.jobboard.util.AppUtils.getCurrentUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final SavedJobRespository savedJobRespository;

    public JobResponseDto createJob(JobRequestDto request) {

        User user = getCurrentUser();
        Job job = Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .location(request.getLocation())
                .type(request.getType())
                .description(request.getDescription())
                .minExperience(request.getMinExperience().setScale(1, RoundingMode.HALF_UP))
                .maxExperience(request.getMaxExperience().setScale(1, RoundingMode.HALF_UP))
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .status(JobStatus.OPEN)
                .recruiter(user)
                .build();
        job = jobRepository.save(job);
        return jobMapper.toDto(job);

    }

    public  JobResponseDto updateJob(JobRequestDto request , UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));
        User user = getCurrentUser();

        if(!job.getRecruiter().getId().equals(user.getId())){
            throw new AccessDeniedException( "You are not authorized to update this job posting");
        }

        jobMapper.updateJob(request, job);
        job.setMinExperience(job.getMinExperience().setScale(1, RoundingMode.HALF_UP));
        job.setMaxExperience(job.getMaxExperience().setScale(1, RoundingMode.HALF_UP));

        Job updatedJob = jobRepository.save(job);

        return jobMapper.toDto(updatedJob);
    }

    public  void deleteJob(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));
        User user = getCurrentUser();

        if(!job.getRecruiter().getId().equals(user.getId())){
            throw new AccessDeniedException( "You are not authorized to delete this job posting");
        }

        jobRepository.delete(job);
    }

    public  List<JobResponseDto> getMyJobs() {

        User user = getCurrentUser();
        List<Job> jobs = jobRepository.findByRecruiterId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job with id '" + user.getId() + "' not found"));

        return jobs.stream()
                .map(jobMapper::toDto)
                .toList();
    }

    public  JobResponseDto getJobById(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));

        return jobMapper.toDto(job);
    }

    public Page<JobResponseDto> filterJobs(JobFilterDto filter, Pageable pageable) {

        Page<Job> jobs = jobRepository.filterJobs(
                filter.getSearch(),
                filter.getLocation(),
                filter.getType(),
                filter.getCompany(),
                filter.getSalaryMin(),
                filter.getSalaryMax(),
                pageable
        );

        return jobs.map(jobMapper::toDto);
    }

    public void apply(JobApplyRequestDto applyRequestDto, UUID jobId) {

        User user = getCurrentUser();

        Job job =  jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));

        if(!job.getStatus().equals(JobStatus.OPEN)){
            throw new JobClosedException("Job is no longer accepting applications");
        }

        boolean alreadyApplied = applicationRepository.existsByApplicantIdAndJobId(user.getId(), jobId);
        if (alreadyApplied) {
            throw new AlreadyAppliedException("You have already applied for this job");
        }

        String resumeUrl = user.getResumeUrl();

        if (resumeUrl == null || resumeUrl.isBlank()) {

            if (applyRequestDto == null ||
                    applyRequestDto.getResumeUrl() == null ||
                    applyRequestDto.getResumeUrl().isBlank()) {

                throw new IllegalStateException(
                        "A resume link is required to apply for this job. Please add it to your profile or provide it in the application."
                );
            }

            resumeUrl = applyRequestDto.getResumeUrl();

            user.setResumeUrl(resumeUrl);
            userRepository.save(user);
        }

        Application application = Application.builder()
                .applicant(user)
                .job(job)
                .resumeUrl(resumeUrl)
                .coverLetter(applyRequestDto != null ? applyRequestDto.getCoverLetter() : null)
                .status(ApplicationStatus.APPLIED)
                .build();

        applicationRepository.save(application);

    }

    public Page<ApplicantResponseDto> getApplicantsForJob(UUID jobId , Pageable pageable){
        User recruiter = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnAuthorizedException("You are not allowed to view applicants for this job");
        }
        Page<Application> applications = applicationRepository.findByJobId(jobId , pageable);

        return applications
                .map(application -> ApplicantResponseDto.builder()
                        .applicantId(application.getApplicant().getId())
                        .applicationId(application.getId())
                        .name(application.getApplicant().getName())
                        .email(application.getApplicant().getEmail())
                        .resumeUrl(application.getResumeUrl())
                        .coverLetter(application.getCoverLetter())
                        .status(application.getStatus())
                        .appliedAt(application.getCreatedAt())
                        .build());
    }

    public void saveJob(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" + jobId + "' not found"));
        User user = getCurrentUser();

        boolean isExists = savedJobRespository.existsByJobIdAndUserId(job.getId() , user.getId());
        if(isExists){
            throw new AlreadySavedJobException("Job is Already Saved");
        }

        SavedJob save = SavedJob.builder()
                .job(job)
                .user(user)
                .build();

        savedJobRespository.save(save);
    }

    public Page<JobResponseDto> getSavedJobs(Pageable pageable) {

        User user = getCurrentUser();

        Page<SavedJob> savedJobs = savedJobRespository.findByUserId(user.getId(), pageable);

        return savedJobs.map(savedJob -> JobResponseDto.builder()
                .id(savedJob.getJob().getId())
                .title(savedJob.getJob().getTitle())
                .company(savedJob.getJob().getCompany())
                .location(savedJob.getJob().getLocation())
                .type(savedJob.getJob().getType())
                .salaryMin(savedJob.getJob().getSalaryMin())
                .salaryMax(savedJob.getJob().getSalaryMax())
                .status(savedJob.getJob().getStatus())
                .createdAt(savedJob.getJob().getCreatedAt())
                .updatedAt(savedJob.getJob().getUpdatedAt())
                .build());
    }

    public void deleteSavedJob(UUID jobId) {;
        User user = getCurrentUser();
        SavedJob savedJob = savedJobRespository.findByJobIdAndUserId(jobId , user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));;
        savedJobRespository.delete(savedJob);
    }
}
