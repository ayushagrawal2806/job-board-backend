package com.ayush.jobboard.dto.Job;


import com.ayush.jobboard.enums.JobStatus;
import lombok.Data;

@Data
public class JobStatusUpdateRequestDto {
    private JobStatus status;

}
