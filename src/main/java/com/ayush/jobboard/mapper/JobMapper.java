package com.ayush.jobboard.mapper;



import com.ayush.jobboard.dto.Job.JobAboutDto;
import com.ayush.jobboard.dto.Job.JobRequestDto;
import com.ayush.jobboard.dto.Job.JobResponseDto;
import com.ayush.jobboard.entity.Job;
import com.ayush.jobboard.entity.JobAbout;
import com.ayush.jobboard.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "recruiterId", source = "recruiter.id")
    JobResponseDto toDto(Job job);

    void updateJob(JobRequestDto dto, @MappingTarget Job job);


    JobAboutDto toDto(JobAbout about);

    JobAbout toEntity(JobAboutDto aboutDto);
}