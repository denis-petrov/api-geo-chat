package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.chat.Chat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public interface ChatService {
    Chat getChat(UUID chatId);

    UUID createChat(String name);

    List<Chat> getAllForUser(UUID userId);

    void updateName(UUID chatId, String newName);

    void remove(UUID chatId);

    void addMember(UUID chatId, UUID userId);

    void removeMember(UUID chatId, UUID userId);
}
