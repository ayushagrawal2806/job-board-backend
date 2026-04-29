package com.ayush.jobboard.event;

import com.ayush.jobboard.entity.Application;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ApplicationCreatedEvent extends ApplicationEvent {

    private final Application application;

    public ApplicationCreatedEvent(Object source, Application application) {
        super(source);
        this.application = application;
    }
}
