package com.app.apigeochat.dto;

import com.app.apigeochat.domain.user.Role;
import com.app.apigeochat.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserProvidingDto {
    private UUID userId;
    private Set<Role> roles;
    private String name;
    private String email;

    public UserProvidingDto(User user) {
        this.userId = user.getUserId();
        this.roles = user.getRoles();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
