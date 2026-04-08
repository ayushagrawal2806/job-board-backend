package com.ayush.jobboard.repository;

import com.ayush.jobboard.entity.Application;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application , UUID> {

    boolean existsByApplicantIdAndJobId(UUID id, UUID jobId);

    Page<Application> findByApplicantId(UUID id , Pageable pageable);

}
