package com.amin.redis.api.users;

import com.amin.redis.tokens.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserResponseMessage {
    private String response;
    private Map<String, ?> message;
}
