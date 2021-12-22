package com.app.apigeochat.dto;

import com.app.apigeochat.domain.user.Role;
import com.app.apigeochat.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserAuthDto {
    private UUID userId;
    private Set<Role> roles;
    private String name;
    private String email;
    private String password;

    public UserAuthDto(User user) {
        this.userId = user.getUserId();
        this.roles = user.getRoles();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
