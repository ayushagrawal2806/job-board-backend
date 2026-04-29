package com.ayush.jobboard.event;

import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.enums.ApplicationStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StatusUpdatedEvent extends ApplicationEvent {

    private final Application application;
    private final ApplicationStatus newStatus;

    public StatusUpdatedEvent(Object source, Application application, ApplicationStatus newStatus) {
        super(source);
        this.application = application;
        this.newStatus = newStatus;
    }
}