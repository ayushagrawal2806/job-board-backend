package com.ayush.jobboard.controller;

import com.ayush.jobboard.common.ApiResponse;
import com.ayush.jobboard.dto.Application.ApplicantResponseDto;
import com.ayush.jobboard.dto.Job.JobApplyRequestDto;
import com.ayush.jobboard.dto.Job.JobFilterDto;
import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<JobResponseDto>>> filterJobs(
            @ModelAttribute JobFilterDto filter,
            Pageable pageable
    ) {
        // when we are sending request there are two params called size and page
        // size : means that in a page how many elements will be there
        // page : page number of the page so it will open that page starts from 0
        Page<JobResponseDto> jobs = jobService.filterJobs(filter, pageable);

        ApiResponse<Page<JobResponseDto>> response = ApiResponse.<Page<JobResponseDto>>builder()
                .message("Jobs fetched successfully")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<ApiResponse<Void>> applyJob(@RequestBody(required = false) JobApplyRequestDto applyRequestDto ,
                                                      @PathVariable UUID jobId){

        jobService.apply(applyRequestDto , jobId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Application submitted successfully")
                .build();


        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/{jobId}/applications")
    public ResponseEntity<ApiResponse<Page<ApplicantResponseDto>>> getAllApplicantsForJob(@PathVariable UUID jobId , Pageable pageable){

       Page<ApplicantResponseDto> applicants = jobService.getApplicantsForJob(jobId , pageable);
        ApiResponse<Page<ApplicantResponseDto>> response = ApiResponse.<Page<ApplicantResponseDto>>builder()
                .message("Applicants fetched successfully")
                .data(applicants)
                .build();


        return ResponseEntity.ok(response);
    }

    // Save job

    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping("/{jobId}/save")
    public ResponseEntity<ApiResponse<Void>> saveJob(@PathVariable UUID jobId){
        jobService.saveJob(jobId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Job saved successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @DeleteMapping("/{jobId}/save")
    public ResponseEntity<ApiResponse<Void>> deleteSavedJob(@PathVariable UUID jobId){
        jobService.deleteSavedJob(jobId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Job unsaved successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @GetMapping("/saved")
    public ResponseEntity<ApiResponse<Page<JobResponseDto>>> getSavedJobs(Pageable pageable) {

        Page<JobResponseDto> jobs = jobService.getSavedJobs(pageable);

        ApiResponse<Page<JobResponseDto>> response = ApiResponse.<Page<JobResponseDto>>builder()
                .message("Saved jobs fetched successfully")
                .data(jobs)
                .build();

        return ResponseEntity.ok(response);
    }


}