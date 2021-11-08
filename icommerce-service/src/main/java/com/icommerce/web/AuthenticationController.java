
package com.icommerce.web;


import com.icommerce.category.model.LoginRequest;
import com.icommerce.category.model.LoginResponse;
import com.icommerce.category.model.RefreshRequest;
import com.icommerce.dom.UserInformation;
import com.icommerce.dom.UserRole;
import com.icommerce.exception.BadRequestException;
import com.icommerce.exception.NotFoundException;
import com.icommerce.exception.TokenExpiredException;
import com.icommerce.security.JwtTokenProvider;
import com.icommerce.service.IUserService;
import com.icommerce.service.LoginService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.openapitools.api.AuthenticateApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class AuthenticationController implements AuthenticateApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;
    private final IUserService userService;


    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        try {
            authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        } catch (Exception exception) {
            throw new BadRequestException("Invalid username/password");
        }

        final UserInformation user = loginService.getUserDisplayName();

        if (!user.isActive()) {
            return ResponseEntity.badRequest().build();
        }

        LoginResponse response = new LoginResponse();
        response.setAccessToken(createAccessToken(loginRequest.getUsername(), user));
        response.setRefreshToken(createRefreshToken(loginRequest.getUsername(), user));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LoginResponse> refreshToken(RefreshRequest refreshRequest) {
        try {
            final String username = jwtTokenProvider.getUsernameFromToken(refreshRequest.getRefreshToken());
            UserInformation user = userService.findByUsername(username);
            if (Objects.isNull(user)) {
                throw new NotFoundException("Invalid user");
            }
            if (Objects.isNull(user.getRefreshToken()) || !user.getRefreshToken().equals(refreshRequest.getRefreshToken())) {
                throw new BadRequestException("Token does not exist");
            }
            if (jwtTokenProvider.isTokenExpired(refreshRequest.getRefreshToken())) {
                throw new TokenExpiredException("Token expired. Please login again!");
            }
            LoginResponse response = new LoginResponse();
            response.setAccessToken(createAccessToken(username, user));
            response.setRefreshToken(refreshRequest.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token expired. Please login again!");
        }
    }

    private String createAccessToken(final String username, UserInformation user) {
        final List<UserRole> roles = Collections.singletonList(user.getRole());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", roles.stream().map(UserRole::getValue).collect(Collectors.toList()));
        return jwtTokenProvider.generateAccessToken(username, claims);
    }

    private String createRefreshToken(final String username, UserInformation user) {
        String token = jwtTokenProvider.generateRefreshToken(username);
        user.setRefreshToken(token);
        userService.updateUserWithoutCheckingPermission(user);
        return token;
    }

    private void authenticate(final String username, final String password) throws Exception {
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)));
        } catch (final DisabledException | BadCredentialsException e) {
            throw new Exception("Authentication failed", e);
        }
    }
}
