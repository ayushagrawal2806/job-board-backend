package com.ayush.jobboard.controller;

import com.ayush.jobboard.common.ApiResponse;
import com.ayush.jobboard.dto.Profile.UpdateProfileRequestDto;
import com.ayush.jobboard.dto.Profile.ProfileResponseDto;
import com.ayush.jobboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profile")
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponseDto>> myProfile() {

        ProfileResponseDto profile = userService.getMyProfile();

        ApiResponse<ProfileResponseDto> response =  ApiResponse.<ProfileResponseDto>builder()
                .message("Profile fetched successfully")
                .data(profile)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponseDto>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto requestDto) {

        ProfileResponseDto profile = userService.updateProfile(requestDto);

        ApiResponse<ProfileResponseDto> response =  ApiResponse.<ProfileResponseDto>builder()
                .message("Profile Updated successfully")
                .data(profile)
                .build();

        return ResponseEntity.ok(response);
    }
}
