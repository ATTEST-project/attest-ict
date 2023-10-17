package com.attest.ict.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    // Custom roles for ATTEST
    public static final String DSO = "ROLE_DSO";

    public static final String TSO = "ROLE_TSO";

    private AuthoritiesConstants() {}
}
