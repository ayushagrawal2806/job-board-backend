package com.ayush.jobboard.controller;

import com.ayush.jobboard.common.ApiResponse;
import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(@PathVariable UUID jobId) {

        JobResponseDto job = jobService.getJobById(jobId);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .message("Job fetched successfully")
                .data(job)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponseDto>> createJob(
            @Valid @RequestBody JobRequestDto jobRequestDto) {

        JobResponseDto job = jobService.createJob(jobRequestDto);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .message("Job created successfully")
                .data(job)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponseDto>> updateJob(
            @Valid @RequestBody JobRequestDto jobRequestDto,
            @PathVariable UUID jobId) {

        JobResponseDto job = jobService.updateJob(jobRequestDto, jobId);

        ApiResponse<JobResponseDto> response = ApiResponse.<JobResponseDto>builder()
                .message("Job updated successfully")
                .data(job)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable UUID jobId) {

        jobService.deleteJob(jobId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Job deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getMyJobs() {

        List<JobResponseDto> jobs = jobService.getMyJobs();

        ApiResponse<List<JobResponseDto>> response = ApiResponse.<List<JobResponseDto>>builder()
                .message("Jobs fetched successfully")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }
}