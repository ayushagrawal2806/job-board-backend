package com.ayush.jobboard.dto.Auth;

import com.ayush.jobboard.dto.Profile.ProfileResponseDto;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.Roles;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({
        "user",
        "accessToken",
        "refreshToken"
})
public class AuthResponseDto {

    private UserResponseDto user;
    private String accessToken;
    private String refreshToken;
}
