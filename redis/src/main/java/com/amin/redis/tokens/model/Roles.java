package com.amin.redis.tokens.model;

public enum Roles {
    ADMIN(1), USER(2);

    private Integer value;

    Roles(int i) {
        this.value = i;
    }
}
