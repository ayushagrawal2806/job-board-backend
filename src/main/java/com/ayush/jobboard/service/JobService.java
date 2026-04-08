package com.ayush.jobboard.service;


import com.ayush.jobboard.dto.Job.JobApplyRequestDto;
import com.ayush.jobboard.dto.Job.JobFilterDto;
import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.ApplicationStatus;
import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.exceptions.AccessDeniedException;
import com.ayush.jobboard.exceptions.AlreadyAppliedException;
import com.ayush.jobboard.exceptions.JobClosedException;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.mapper.JobMapper;
import com.ayush.jobboard.repository.ApplicationRepository;
import com.ayush.jobboard.repository.JobRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.math.RoundingMode;
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
            throw new AccessDeniedException( "You are not authorized to update this job posting");
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





        Application application = Application.builder()
                .applicant(user)
                .job(job)
                .resumeUrl(applyRequestDto != null ? applyRequestDto.getResumeUrl() : null)
                .coverLetter(applyRequestDto != null ? applyRequestDto.getCoverLetter() : null)
                .status(ApplicationStatus.APPLIED)
                .build();

        applicationRepository.save(application);

    }
}
