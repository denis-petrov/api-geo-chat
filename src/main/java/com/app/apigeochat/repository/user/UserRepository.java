package com.app.apigeochat.repository.user;

import com.app.apigeochat.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User getByName(String name);
    User getByEmail(String email);
}
