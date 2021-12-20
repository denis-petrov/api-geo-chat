package com.app.apigeochat.repository.chat;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByMembersContains(User member);
    boolean existsByName(String name);
}
