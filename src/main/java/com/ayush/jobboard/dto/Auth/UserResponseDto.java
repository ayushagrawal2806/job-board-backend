package com.ayush.jobboard.dto.Auth;

import com.ayush.jobboard.enums.Roles;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "role",
        "phone",
        "location",
        "resumeUrl",
        "bio",
        "createdAt",
        "updatedAt"
})
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Roles role;

    private String phone;
    private String location;

    // seeker related fields
    private String resumeUrl;
    private String bio;
}
