
package com.icommerce.security;


import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        final String jwtToken = getTokenFromRequest(request);
        if (StringUtils.isEmpty(jwtToken)) throw new IllegalArgumentException("Cannot find jwt token");
        final String username = jwtTokenProvider.getUsernameFromToken(jwtToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }


        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(final HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(authHeader)) {
            return StringUtils.startsWith(authHeader, "Bearer") ? authHeader.substring(7) : authHeader;
        }
        return StringUtils.EMPTY;
    }


    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return Stream.of("/", "/authenticate/login", "/authenticate/refresh")
                .anyMatch(p -> Pattern.matches(p, request.getServletPath()));
    }


}
