package com.amin.redis.api.tokens;

import com.amin.redis.tokens.model.UserToken;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;


@Data
@AllArgsConstructor
public class TokenResponseMessage {
    private String message;
    private Map<String, UserToken> token;
}