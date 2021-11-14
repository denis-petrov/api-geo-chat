package com.app.apigeochat.service.chat.impl;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.ChatRepository;
import com.app.apigeochat.repository.UserRepository;
import com.app.apigeochat.service.chat.ChatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepo;
    private final UserRepository userRepo;

    public ChatServiceImpl(ChatRepository chatRepo, UserRepository userRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Chat getChat(UUID chatId) {
        return chatRepo.getById(chatId);
    }

    @Override
    public List<Chat> getAllForUser(UUID userId) {
        User user = new User();
        user.setUserId(userId);
        return chatRepo.findByMembersContains(user);
    }

    @Override
    public void updateName(UUID chatId, String newName) {
        Chat chat = chatRepo.getById(chatId);
        chat.setName(newName);
        chatRepo.save(chat);
    }

    @Override
    public void remove(UUID chatId) {
        chatRepo.deleteById(chatId);
    }

    @Override
    public void addMember(UUID chatId, UUID userId) {
        Chat chat = chatRepo.getById(chatId);
        User user = userRepo.getById(userId);
        chat.getMembers().add(user);
    }

    @Override
    public void removeMember(UUID chatId, UUID userId) {
        Chat chat = chatRepo.getById(chatId);
        chat.getMembers().removeIf(user -> user.getUserId() == userId);
    }

    @Override
    public UUID createChat(String name) {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setChatId(null);
        return chatRepo.save(chat).getChatId();
    }
}
