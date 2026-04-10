package com.ayush.jobboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "saved_jobs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"job_id", "user_id"})
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private Instant createdAt;
}