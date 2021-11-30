package com.app.apigeochat.repository;

import com.app.apigeochat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User getByName(String name);
    User getByEmail(String email);
}
