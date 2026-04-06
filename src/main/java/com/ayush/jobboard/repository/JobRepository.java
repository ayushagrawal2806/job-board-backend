package com.ayush.jobboard.repository;

import com.ayush.jobboard.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job , UUID> {
     Optional<List<Job>> findByRecruiterId(UUID id);
}
