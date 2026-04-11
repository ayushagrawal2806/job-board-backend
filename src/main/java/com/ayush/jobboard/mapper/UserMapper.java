package com.ayush.jobboard.mapper;

import com.ayush.jobboard.dto.Auth.UserResponseDto;
import com.ayush.jobboard.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);
}
