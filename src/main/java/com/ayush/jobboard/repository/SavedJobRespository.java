package com.ayush.jobboard.repository;

import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.SavedJob;
import com.ayush.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SavedJobRespository extends JpaRepository<SavedJob , UUID> {

    boolean existsByJobIdAndUserId(UUID id, UUID id1);

    Optional<SavedJob> findByJobIdAndUserId(UUID jobId, UUID id);

    Page<SavedJob> findByUserId(UUID id, Pageable pageable);
}
