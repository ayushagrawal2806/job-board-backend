package com.ayush.jobboard.dto.Application;

import com.ayush.jobboard.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({
        "applicationId",
        "jobId",
        "jobTitle",
        "company",
        "location",
        "applicantId",
        "applicantName",
        "applicantEmail",
        "resumeUrl",
        "coverLetter",
        "status",
        "appliedAt"
})
public class ApplicationDetailResponseDto {

    private UUID applicationId;

    private UUID jobId;
    private String jobTitle;
    private String company;
    private String location;

    private UUID applicantId;
    private String applicantName;
    private String applicantEmail;

    private String resumeUrl;
    private String coverLetter;

    private ApplicationStatus status;
    private Instant appliedAt;
}