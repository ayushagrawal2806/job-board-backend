package com.ayush.jobboard.dto.Application;

import com.ayush.jobboard.enums.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationUpdateRequestDto {

    private ApplicationStatus status;
}
