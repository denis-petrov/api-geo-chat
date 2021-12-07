package com.app.apigeochat.repository;

import com.app.apigeochat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
