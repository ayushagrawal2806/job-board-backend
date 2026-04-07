package com.ayush.jobboard.service;


import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.exceptions.AccessDeniedException;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.mapper.JobMapper;
import com.ayush.jobboard.repository.JobRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

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
}
