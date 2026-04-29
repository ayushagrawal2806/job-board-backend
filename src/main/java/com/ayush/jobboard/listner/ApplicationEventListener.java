package com.ayush.jobboard.listner;

import com.ayush.jobboard.event.ApplicationCreatedEvent;
import com.ayush.jobboard.event.StatusUpdatedEvent;
import com.ayush.jobboard.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicationCreated(ApplicationCreatedEvent event) {
       try {
           var application = event.getApplication();
           var applicant = application.getApplicant();
           var job = application.getJob();


           emailService.sendEmail(
                   applicant.getEmail(),
                   "Application Received – " + job.getTitle(),
                   "Hi " + applicant.getName() + ",\n\n" +
                           "Great news! Your application for the position of " + job.getTitle() + " at " + job.getCompanyName() + " has been successfully received.\n\n" +
                           "Our team will review your application and get back to you shortly.\n\n" +
                           "Best of luck!\n" +
                           "Team CareerPulse"
           );


           emailService.sendEmail(
                   job.getRecruiter().getEmail(),
                   "New Applicant for " + job.getTitle(),
                   "Hi,\n\n" +
                           "You have a new applicant for your job posting – " + job.getTitle() + ".\n\n" +
                           "Applicant: " + applicant.getName() + "\n" +
                           "Email: " + applicant.getEmail() + "\n\n" +
                           "Login to CareerPulse to review their application and resume.\n\n" +
                           "Team CareerPulse"
           );
       }catch (Exception e){
           log.error("Failed to send application created email: {}", e.getMessage(), e);
       }
    }

    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStatusUpdated(StatusUpdatedEvent event) {
    try  {
        var application = event.getApplication();
        var applicant = application.getApplicant();
        var job = application.getJob();

        emailService.sendEmail(
                applicant.getEmail(),
                "Application Status Update – " + job.getTitle(),
                "Hi " + applicant.getName() + ",\n\n" +
                        "Your application for " + job.getTitle() + " at " + job.getCompanyName() + " has been reviewed.\n\n" +
                        "Current Status: " + event.getNewStatus() + "\n\n" +
                        "Login to CareerPulse to view the full details of your application.\n\n" +
                        "Best regards,\n" +
                        "Team CareerPulse"
        );
    } catch (Exception e) {
        log.error("Failed to send status updated email: {}", e.getMessage(), e);
    }
    }
}