package com.app.apigeochat.repository;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByMembersContains(User member);
}
