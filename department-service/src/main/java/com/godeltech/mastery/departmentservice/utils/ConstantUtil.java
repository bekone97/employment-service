package com.godeltech.mastery.departmentservice.utils;

public class ConstantUtil {
    public static class Exception {
        public final static String NO_FOUNDED_PATTERN = "%s wasn't found by %s=%s";
    }

    public static class JwtToken {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String CLAIM_ROLES = "roles";
    }


}
