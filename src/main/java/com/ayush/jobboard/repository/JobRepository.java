package com.ayush.jobboard.repository;

import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job , UUID> {
     Optional<List<Job>> findByRecruiterId(UUID id);

    @Query("""
    SELECT j FROM Job j
    WHERE (:search IS NULL OR LOWER(j.title) LIKE %:search%)
    AND (:location IS NULL OR LOWER(j.location) = LOWER(CAST(:location AS string)))
    AND (:type IS NULL OR j.type = :type)
    AND (:companyName IS NULL OR j.companyName = :companyName)
    AND (:salaryMin IS NULL OR j.salaryMin >= :salaryMin)
    AND (:salaryMax IS NULL OR j.salaryMax <= :salaryMax)
    """)
    Page<Job> filterJobs(
            String search,
            String location,
            JobType type,
            String companyName,
            Integer salaryMin,
            Integer salaryMax,
            Pageable pageable
    );
}
