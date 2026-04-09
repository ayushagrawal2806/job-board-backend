package com.ayush.jobboard.mapper;

import com.ayush.jobboard.dto.Profile.ProfileResponseDto;
import com.ayush.jobboard.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponseDto toDto(User user);
}
