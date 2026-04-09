package com.ayush.jobboard.dto.Auth;

import com.ayush.jobboard.enums.Roles;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
//    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
//            message = "Password must contain uppercase, lowercase, number and special character"
//    )
    private String password;

    @NotNull(message = "Role is required")
    private Roles role;
}
