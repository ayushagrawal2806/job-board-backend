package com.ayush.jobboard.controller;

import com.ayush.jobboard.common.ApiResponse;
import com.ayush.jobboard.dto.Auth.AuthResponseDto;
import com.ayush.jobboard.dto.Auth.LoginRequestDto;
import com.ayush.jobboard.dto.Auth.SignupRequestDto;
import com.ayush.jobboard.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto user , HttpServletResponse response){
        AuthResponseDto responseDto = authService.login(user , response);
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("User logged in successfully")
                .data(responseDto)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping(path = "/signup")
    public ResponseEntity<ApiResponse<AuthResponseDto>> signUp(@Valid @RequestBody SignupRequestDto user , HttpServletResponse response){
        AuthResponseDto responseDto = authService.signUp(user , response);
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("User registered successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>>  refresh(HttpServletRequest request){
        String refreshToken =   Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token is not present"));
        AuthResponseDto responseDto = authService.refresh(refreshToken);
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("Access token refreshed successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
