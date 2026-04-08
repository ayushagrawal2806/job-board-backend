package com.ayush.jobboard.controller;


import com.ayush.jobboard.common.ApiResponse;

import com.ayush.jobboard.dto.Application.ApplicationResponseDto;
import com.ayush.jobboard.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping(path = "/applications")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PreAuthorize("hasRole('SEEKER')")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ApplicationResponseDto>>> myAppliedJobs(Pageable pageable){
        Page<ApplicationResponseDto> appliedJobs = applicationService.getMyApplications(pageable);

        // when we are sending request there are two params called size and page
        // size : means that in a page how many elements will be there
        // page : page number of the page so it will open that page starts from 0

        ApiResponse<Page<ApplicationResponseDto>> response = ApiResponse.<Page<ApplicationResponseDto>>builder()
                .message("Applications fetched successfully")
                .data(appliedJobs)
                .build();

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('SEEKER')")
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(@PathVariable UUID applicationId){
        applicationService.withdrawApplication(applicationId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Application withdrawn successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
