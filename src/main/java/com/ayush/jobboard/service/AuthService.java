package com.ayush.jobboard.service;

import com.ayush.jobboard.Security.JWTService;
import com.ayush.jobboard.dto.Auth.AuthResponseDto;
import com.ayush.jobboard.dto.Auth.LoginRequestDto;
import com.ayush.jobboard.dto.Auth.SignupRequestDto;
import com.ayush.jobboard.dto.Auth.UserResponseDto;
import com.ayush.jobboard.dto.Profile.ProfileResponseDto;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.mapper.ProfileMapper;
import com.ayush.jobboard.mapper.UserMapper;
import com.ayush.jobboard.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDto login( LoginRequestDto loginRequestDto , HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail() , loginRequestDto.getPassword())
        );

        User result = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = userMapper.toDto(result);

        String accessToken = jwtService.generateAccessToken(result);
        String refreshToken = jwtService.generateRefreshToken(result);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponseDto)
//                .id(result.getId())
//                .name(result.getName())
//                .email(result.getEmail())
//                .role(result.getRole())
                .build();
    }

    public AuthResponseDto signUp(SignupRequestDto signUpRequest , HttpServletResponse response) {
        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(existingUser -> {
            throw new BadCredentialsException("User already exists");
        });;

        User result = User.builder()
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .role(signUpRequest.getRole())
                .build();
        userRepository.save(result);
        // here we are doing auto login
        return login(
                LoginRequestDto.builder()
                        .email(signUpRequest.getEmail())
                        .password(signUpRequest.getPassword())
                        .build(),
                response
        );
    };

    public  AuthResponseDto refresh(String refreshToken) {

        UUID userIdFromToken = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userIdFromToken).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        UserResponseDto userResponseDto = userMapper.toDto(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponseDto)
//                .id(user.getId())
//                .name(user.getName())
//                .email(user.getEmail())
                .build();


    }
}
