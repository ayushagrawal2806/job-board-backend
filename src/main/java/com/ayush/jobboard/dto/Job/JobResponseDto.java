package com.ayush.jobboard.dto.Job;

import com.ayush.jobboard.enums.JobStatus;
import com.ayush.jobboard.enums.JobType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "recruiterId",
        "title",
        "company",
        "location",
        "type",
        "description",
        "salaryMin",
        "salaryMax",
        "status",
        "createdAt",
        "updatedAt"
})
public class JobResponseDto {

    private UUID id;
    private UUID recruiterId;
    private String title;
    private String company;
    private String location;
    private JobType type;
    private String description;
    private Integer salaryMin;
    private Integer salaryMax;
    private JobStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}