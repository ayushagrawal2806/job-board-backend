package com.ayush.jobboard.service;

import com.ayush.jobboard.dto.Job.CreateJobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.mapper.JobMapper;
import com.ayush.jobboard.repository.JobRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ayush.jobboard.util.AppUtils.getCurrentUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;


    public JobResponseDto createJob(CreateJobRequestDto request) {

        User user = getCurrentUser();
        Job job = Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .location(request.getLocation())
                .type(request.getType())
                .description(request.getDescription())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .status(JobStatus.OPEN)
                .recruiter(user)
                .build();
        job = jobRepository.save(job);
        return jobMapper.toDto(job);

    }

    public  JobResponseDto updateJob(CreateJobRequestDto request , UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No job found with id : " + id));
        User user = getCurrentUser();

        if(!job.getRecruiter().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to update this job");
        }

        jobMapper.updateJob(request, job);

        Job updatedJob = jobRepository.save(job);

        return jobMapper.toDto(updatedJob);
    }
}
