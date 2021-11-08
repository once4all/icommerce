
package com.icommerce.security;


import com.icommerce.dom.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static JwtUserDetails getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        final Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userDetails instanceof JwtUserDetails)) {
            throw new UnsupportedOperationException("Only support Jwt Authentication");
        }

        return (JwtUserDetails) userDetails;
    }

    public static Long getLoginId() {
        return getUserDetails().getLoginId();
    }

    public static String getUsername() {
        return getUserDetails().getUsername();
    }

    public static boolean hasRole(final String role) {
        final List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return currentRoles.contains(role);
    }

    public static boolean hasRole(final UserRole role) {
        return hasRole(role.name());
    }

    public static boolean hasAnyRoles(final Collection<UserRole> roles) {
        return roles.stream().anyMatch(SecurityUtils::hasRole);
    }

    public static List<String> getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
    }


    public static boolean hasAnyRoles(final String... checkRoles) {
        return getRoles().stream()
                .anyMatch(e -> Arrays.stream(checkRoles)
                        .anyMatch(r -> StringUtils.equalsIgnoreCase(e, r))
                );
    }

}
