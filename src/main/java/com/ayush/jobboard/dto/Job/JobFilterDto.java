package com.ayush.jobboard.dto.Job;

import com.ayush.jobboard.enums.JobType;
import lombok.Data;

@Data
public class JobFilterDto {

    private String search;
    private String location;
    private JobType type;
    private Integer salaryMin;
    private Integer salaryMax;
    private String company;
}
