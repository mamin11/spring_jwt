package com.amin.redis.api.users;

import com.amin.redis.api.exception.ApiException;
import com.amin.redis.api.util.ApiProperties;
import com.amin.redis.tokens.model.Roles;
import com.amin.redis.tokens.model.User;
import com.amin.redis.tokens.repository.user.UserRepositoryImp;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static com.amin.redis.api.AuthFilter.AuthenticationFilter.ALGORITHM;
import static com.amin.redis.api.AuthFilter.AuthorizationFilter.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping() @CrossOrigin(origins = "*") @Slf4j @Validated
public class UserController {
    @Autowired UserRepositoryImp userRepositoryImp;

    @GetMapping(ApiProperties.ALL_USERS)
    public ResponseEntity<UserResponseMessage> all() {
        Map<String, User> users = userRepositoryImp.findAll();
        return ResponseEntity.status(200).body(new UserResponseMessage("success", users));
    }

    @GetMapping(ApiProperties.FIND_BY_EMAIL)
    public ResponseEntity<UserResponseMessage> find(@RequestBody String email) {
        User user = userRepositoryImp.findByEmail(email);
        Map<String, User> response = new HashMap<>();
        response.put("user", user);
        return ResponseEntity.status(200).body(new UserResponseMessage("success", response));
    }

    @PostMapping(ApiProperties.ADD_USER)
    public ResponseEntity<UserResponseMessage> add(@Valid @RequestBody User user) throws ApiException {
        log.debug("request: {}", user);
            try {
                userRepositoryImp.save(user);
            } catch (Exception e) {
                log.debug("exception: {}", e.getMessage());
                Map<String, List<String>> body = new HashMap<>();
                body.put("exception", Collections.singletonList(e.getMessage()));
                return ResponseEntity.status(400).body(new UserResponseMessage("error", body));
            }
            return ResponseEntity.status(200).body(new UserResponseMessage("success", userRepositoryImp.findAll()));
    }

    @PostMapping(ApiProperties.DELETE_ALL_USERS)
    public ResponseEntity<String> delete() {
        log.debug("Deleting all users");
        try {
            userRepositoryImp.deleteAll();
        } catch (Exception e) {
            log.debug("Exception happened: {}", e.getMessage());
        }
        return ResponseEntity.status(200).body("Successfully deleted all");
    }

    @GetMapping(ApiProperties.REFRESH_TOKEN)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            try {
                String refreshToken = authorizationHeader.substring(BEARER.length());
                JWTVerifier verifier = JWT.require(ALGORITHM).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userEmail = decodedJWT.getSubject();

                User user = userRepositoryImp.findByEmail(userEmail);

                assert user != null;

                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + ApiProperties.TOKEN_EXPIRY))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRole().stream().map(Roles::toString).collect(Collectors.toList()))
                        .sign(ALGORITHM);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                log.error("Refresh token failed: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
