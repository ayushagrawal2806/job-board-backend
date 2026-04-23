package com.ayush.jobboard.dto.Job;

import com.ayush.jobboard.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobRequestDto {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "company name is required")
    private String companyName;

    @NotBlank(message = "location is required")
    private String location;

    @NotNull(message = "job type is required")
    private JobType type;

    @NotNull(message = "about is required")
    private JobAboutDto about;

    @NotNull(message = "min experience is required")
    private BigDecimal minExperience;

    @NotNull(message = "max experience is required")
    private BigDecimal maxExperience;

    private Integer salaryMin;

    private Integer salaryMax;
}