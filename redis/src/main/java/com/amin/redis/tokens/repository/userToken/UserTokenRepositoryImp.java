package com.amin.redis.tokens.repository.userToken;

import com.amin.redis.tokens.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserTokenRepositoryImp implements UserTokenRepository{
    @Autowired
    private RedisTemplate<String, UserToken> redisTemplate;

    public UserTokenRepositoryImp(RedisTemplate<String, UserToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    private HashOperations hashOperations;

    @Override
    public void save(UserToken userToken) {
        hashOperations.put("USER_TOKEN", userToken.getId(), userToken);
    }

    @Override
    public Map<String, UserToken> findAll() {
        return hashOperations.entries("USER_TOKEN");
    }
}
