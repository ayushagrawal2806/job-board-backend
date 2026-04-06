package com.ayush.jobboard.util;

import com.ayush.jobboard.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

    public static User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("Session expired. Please login again.");
        }

        return (User) authentication.getPrincipal();
    }
}
