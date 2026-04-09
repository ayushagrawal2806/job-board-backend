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
        "applicantId",
        "name",
        "email",
        "resumeUrl",
        "coverLetter",
        "status",
        "appliedAt"
})
public class ApplicantResponseDto {

    private UUID applicantId;
    private UUID applicationId;
    private String name;
    private String email;
    private String resumeUrl;
    private String coverLetter;
    private ApplicationStatus status;
    private Instant appliedAt;

}
