package com.icommerce.dom;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    UserRole(String value) {
        this.value = value;
    }

    private final String value;
    private static final Map<String, UserRole> map = new HashMap<>();

    static {
        for (UserRole u : UserRole.values()) {
            map.put(u.getValue(), u);
        }
    }

    public static UserRole fromValue(String s) {
        return map.getOrDefault(s, null);
    }
}
