package com.amin.redis.tokens.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Collection;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @RedisHash("Users")
public class User implements Serializable {
    @NotEmpty(message = "Please provide email") @NonNull private String email;
    @NotEmpty(message = "Please provide password") @NonNull private String password;
    @NotEmpty(message = "Please provide role") @NonNull private Collection<Roles> role;
}
