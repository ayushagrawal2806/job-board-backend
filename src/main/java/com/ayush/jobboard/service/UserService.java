package com.ayush.jobboard.service;

import com.ayush.jobboard.dto.Profile.UpdateProfileRequestDto;
import com.ayush.jobboard.dto.Profile.ProfileResponseDto;
import com.ayush.jobboard.entity.User;
import com.ayush.jobboard.enums.Roles;
import com.ayush.jobboard.exceptions.ResourceNotFoundException;
import com.ayush.jobboard.mapper.ProfileMapper;
import com.ayush.jobboard.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ayush.jobboard.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + username));
    }

    public User getUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public ProfileResponseDto getMyProfile() {
        User user = getCurrentUser();
        return profileMapper.toDto(user);
    }

    public ProfileResponseDto updateProfile(@Valid UpdateProfileRequestDto requestDto) {

        User user = getCurrentUser();

        user.setName(requestDto.getName());
        user.setPhone(requestDto.getPhone());
        user.setLocation(requestDto.getLocation());

        if (user.getRole() == Roles.SEEKER) {

            if (requestDto.getResumeUrl() == null || requestDto.getResumeUrl().isBlank()) {
                throw new IllegalArgumentException("Resume URL is required for seekers");
            }

            if (requestDto.getBio() == null || requestDto.getBio().isBlank()) {
                throw new IllegalArgumentException("Bio is required for seekers");
            }

            user.setResumeUrl(requestDto.getResumeUrl());
            user.setBio(requestDto.getBio());
        }

        User result =  userRepository.save(user);

        return profileMapper.toDto(result);

    }
}
