package com.example.CarRentalSystem.config.utils;

/**
 * Utility class that defines authorization roles and the corresponding access rights for an online school application.
 */
public class AuthorizationRightsRoles {

    /**
     * Role for regular users.
     */
    public static final String USER = "USER";

    /**
     * Role for administrators.
     */
//    public static final String ADMIN = "ADMIN";

    /**
     * List of endpoints accessible to users with the USER role.
     */
    public static final String[] USER_LIST = {
            "/booking/*",
            "/searchService/*",
    };

    /**
     * List of endpoints accessible to users with the ADMIN role.
     */
//    public static final String[] ADMIN_LIST = {
//            "/roles/**",
//            "/authority/**",
//            "/log",
//            "/user_infos/addRole"
//    };
}
