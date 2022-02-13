package com.amin.redis.tokens.repository.user;

import com.amin.redis.tokens.model.User;
import java.util.Map;

public interface UserRepository {
    void save(User user);
    void deleteAll();
    Map<String, User> findAll();
    User findByEmail(String email);
}
