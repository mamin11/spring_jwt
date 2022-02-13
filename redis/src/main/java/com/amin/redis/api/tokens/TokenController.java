package com.amin.redis.api.tokens;

import com.amin.redis.tokens.model.UserToken;
import com.amin.redis.tokens.repository.userToken.UserTokenRepositoryImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "/api/token")
@CrossOrigin(origins = "*")
@Slf4j
public class TokenController {
    @Autowired
    UserTokenRepositoryImp userTokenRepository;

    @GetMapping("/all")
    public ResponseEntity<TokenResponseMessage> all() {
        Map<String, UserToken> userTokens = userTokenRepository.findAll();
        return ResponseEntity.status(200).body(new TokenResponseMessage("success", userTokens));
    }

    @PostMapping("/add")
    public ResponseEntity<TokenResponseMessage> add(@RequestBody UserToken userToken) {
        userTokenRepository.save(userToken);
        return ResponseEntity.status(200).body(new TokenResponseMessage("success", userTokenRepository.findAll()));
    }
}
