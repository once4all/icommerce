
package com.icommerce.security;

import com.icommerce.dom.UserInformation;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class JwtUserDetails extends User {

    public static final String ADMIN_LOGIN = "admin";

    private Long loginId;
    private final UserInformation userInfo;

    public JwtUserDetails(UserInformation userInfo) {
        super(userInfo.getUsername(), userInfo.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole().getValue())));
        this.userInfo = userInfo;
    }

    public JwtUserDetails loginId(final Long loginId) {
        this.loginId = loginId;
        return this;
    }
}
