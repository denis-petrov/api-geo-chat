package com.app.apigeochat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCreationDto {
    private String name;
    private String password;
    private String email;
}
