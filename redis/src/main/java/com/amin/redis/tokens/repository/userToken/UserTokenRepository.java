package com.amin.redis.tokens.repository.userToken;

import com.amin.redis.tokens.model.UserToken;
import java.util.Map;

public interface UserTokenRepository {
    void save(UserToken userToken);
    Map<String, UserToken> findAll();
}
