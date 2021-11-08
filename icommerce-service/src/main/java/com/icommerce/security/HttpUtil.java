package com.icommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Component
public class HttpUtil {

    @Autowired
    private Environment environment;

    public void setCookie(final HttpServletResponse response, final String cookieName, final String cookieValue, final int cookieMaxAge) {
        final Cookie cookie = new Cookie(cookieName, cookieValue);
        final boolean isDevProfile = Arrays.stream(environment.getActiveProfiles()).anyMatch("dev"::equalsIgnoreCase);
        cookie.setSecure(!isDevProfile);
        cookie.setHttpOnly(!isDevProfile);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
