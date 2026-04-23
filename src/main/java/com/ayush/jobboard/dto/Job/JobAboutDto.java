package com.ayush.jobboard.dto.Job;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobAboutDto {

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "responsibilities is required")
    private String responsibilities;

    @NotBlank(message = "requirements is required")
    private String requirements;

    private String benefits;

    private String companyAbout;

    @NotBlank(message = "skills is required")
    private String skills;
}