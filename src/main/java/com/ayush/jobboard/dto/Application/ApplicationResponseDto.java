package com.ayush.jobboard.dto.Application;

import com.ayush.jobboard.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ApplicationResponseDto {

    private UUID applicationId;
    private UUID jobId;
    private String jobTitle;
    private String company;
    private String location;
    private ApplicationStatus status;
    private Instant appliedAt;

}
