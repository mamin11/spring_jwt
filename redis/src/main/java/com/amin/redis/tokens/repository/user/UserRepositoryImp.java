package com.amin.redis.tokens.repository.user;

import com.amin.redis.tokens.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImp implements UserRepository, UserDetailsService {
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryImp(RedisTemplate<String, User> redisTemplate, PasswordEncoder passwordEncoder) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
        this.passwordEncoder = passwordEncoder;
    }

    private HashOperations hashOperations;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        hashOperations.put("USER", user.getEmail(), user);
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete("USER");
    }

    @Override
    public Map<String, User> findAll() {
        return hashOperations.entries("USER");
    }

    @Override
    public User findByEmail(String email) {
        User user = (User) hashOperations.entries("USER").get(email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) hashOperations.entries("USER").get(username);
        if (user == null) {
            log.error("User not found: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        assert user != null;
        user.getRole().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.name())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
