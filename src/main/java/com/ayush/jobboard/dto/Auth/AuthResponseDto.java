package com.ayush.jobboard.dto.Auth;

import com.ayush.jobboard.enums.Roles;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "role",
        "accessToken",
        "refreshToken"
})
public class AuthResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Roles role;
    private String accessToken;
    private String refreshToken;
}
