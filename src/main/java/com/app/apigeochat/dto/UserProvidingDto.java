package com.app.apigeochat.dto;

import com.app.apigeochat.domain.Role;
import com.app.apigeochat.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserProvidingDto {
    private UUID userId;
    private Role role;
    private String name;
    private String email;

    public UserProvidingDto(User user) {
        this.userId = user.getUserId();
        this.role = user.getRole();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
