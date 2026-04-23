package com.ayush.jobboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAbout {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responsibilities;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(columnDefinition = "TEXT")
    private String companyAbout;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String skills;
}