package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.ChatRepository;
import com.app.apigeochat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class ChatService {
    private final ChatRepository chatRepo;
    private final UserRepository userRepo;

    @Autowired
    public ChatService(ChatRepository chatRepo, UserRepository userRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    public Optional<Chat> getChat(UUID chatId) {
        return chatRepo.findById(chatId);
    }

    public List<Chat> getAllForUser(UUID userId) {
        User user = new User();
        user.setUserId(userId);
        return chatRepo.findByMembersContains(user);
    }

    public boolean updateName(UUID chatId, String newName) {
        Optional<Chat> optionalChat = chatRepo.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setName(newName);
            chatRepo.save(chat);
        });

        return optionalChat.isPresent();
    }

    public boolean remove(UUID chatId) {
        return chatRepo.findAll().removeIf(chat -> chat.getChatId() == chatId);
    }

    public boolean addMember(UUID chatId, UUID userId) {
        Optional<Chat> chat = chatRepo.findById(chatId);
        Optional<User> user = userRepo.findById(userId);
        if (chat.isPresent() && user.isPresent()) {
            chat.get().getMembers().add(user.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean removeMember(UUID chatId, UUID userId) {
        Optional<Chat> optionalChat = chatRepo.findById(chatId);
        optionalChat.ifPresent(chat ->
                chat.getMembers().removeIf(user -> user.getUserId() == userId));
        return optionalChat.isPresent();
    }

    public UUID createChat(String name) {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setChatId(null);
        return chatRepo.save(chat).getChatId();
    }
}
