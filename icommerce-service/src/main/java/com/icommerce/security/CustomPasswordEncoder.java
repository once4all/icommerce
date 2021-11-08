package com.icommerce.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString()).equals(encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(new CustomPasswordEncoder().encode("test"));
    }
}
