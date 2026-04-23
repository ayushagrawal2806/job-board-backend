package com.ayush.jobboard.dto.Job;

import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.enums.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "recruiterId",
        "title",
        "companyName",
        "location",
        "type",
        "about",
        "minExperience",
        "maxExperience",
        "salaryMin",
        "salaryMax",
        "status",
        "createdAt",
        "updatedAt"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResponseDto {

    private UUID id;
    private UUID recruiterId;
    private String title;
    private String companyName;
    private String location;
    private JobType type;
    private JobAboutDto about;
    private BigDecimal minExperience;
    private BigDecimal maxExperience;
    private Integer salaryMin;
    private Integer salaryMax;
    private JobStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}