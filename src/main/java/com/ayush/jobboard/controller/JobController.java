package com.ayush.jobboard.controller;

import com.ayush.jobboard.dto.Job.CreateJobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RequestMapping(path = "/job")
@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody CreateJobRequestDto createJobRequestDto){
        return ResponseEntity.ok(jobService.createJob(createJobRequestDto));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<JobResponseDto> updateJob(@Valid @RequestBody CreateJobRequestDto createJobRequestDto , @PathVariable UUID id){
        return ResponseEntity.ok(jobService.updateJob(createJobRequestDto , id));
    }
}
