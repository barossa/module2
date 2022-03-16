package com.epam.esm.util;

import com.epam.esm.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static List<String> getCurrentRoles() {
        Set<String> roles = getCurrentUser().getRoles();
        return new ArrayList<>(roles);
    }

    public static UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = new UserDto();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDto) {
                userDto = (UserDto) principal;
            }
        }
        return userDto;
    }
}
