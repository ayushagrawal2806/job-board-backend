package com.ayush.jobboard.dto.Job;

import lombok.Data;

import java.util.UUID;

@Data
public class JobApplyRequestDto {

    private String resumeUrl;
    private String coverLetter;

}
