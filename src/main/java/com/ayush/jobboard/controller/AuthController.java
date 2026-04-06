package com.ayush.jobboard.controller;

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

import java.util.Arrays;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto user , HttpServletResponse response){
        return ResponseEntity.ok(authService.login(user , response));
    }


    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponseDto> signUp(@Valid @RequestBody SignupRequestDto user , HttpServletResponse response){
        return  new ResponseEntity<>(authService.signUp(user , response) , HttpStatus.CREATED);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthResponseDto> refresh(HttpServletRequest request){
        String refreshToken =   Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("refresh token is not present"));
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
