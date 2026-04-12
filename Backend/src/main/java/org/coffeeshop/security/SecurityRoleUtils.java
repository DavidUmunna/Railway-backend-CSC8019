package org.coffeeshop.security;

import java.util.Locale;

public final class SecurityRoleUtils {

    private static final String ROLE_PREFIX = "ROLE_";

    private SecurityRoleUtils() {
    }

    public static String toAuthority(String role) {
        String normalized = role == null ? "USER" : role.trim().toUpperCase(Locale.ROOT);
        if (!normalized.startsWith(ROLE_PREFIX)) {
            normalized = ROLE_PREFIX + normalized;
        }
        return normalized;
    }
}
