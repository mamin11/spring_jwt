package com.amin.redis.tokens.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @RedisHash("UserTokens")
public class UserToken implements Serializable {
    private String id;
    private String token;
}
