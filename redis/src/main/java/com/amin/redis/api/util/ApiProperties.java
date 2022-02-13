package com.amin.redis.api.util;

public class ApiProperties {
    public static final String LOGIN = "/api/login";
    public static final String ALL_USERS = "/api/user/all";
    public static final String ALL_TOKENS = "/api/token/all";
    public static final String FIND_BY_EMAIL = "/api/user/email";
    public static final String ADD_USER = "/api/user/add";
    public static final String DELETE_ALL_USERS = "/api/user/delete/all";
    public static final String REFRESH_TOKEN = "/api/user/refreshToken";
    public static final Integer TOKEN_EXPIRY = 24*60*60*1000;
    public static final Integer REFRESH_TOKEN_EXPIRY = 7*24*60*60*1000;
}
