package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.repository.chat.ChatRepository;
import com.app.apigeochat.repository.user.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        var user = new User();
        user.setUserId(userId);
        return chatRepo.findByMembersContains(user);
    }

    public boolean updateName(UUID chatId, String newName) {
        var optionalChat = chatRepo.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setName(newName);
            chatRepo.save(chat);
        });

        return optionalChat.isPresent();
    }

    public void remove(UUID chatId) {
        chatRepo.deleteById(chatId);
    }

    public boolean addMember(UUID chatId, UUID userId) {
        var chat = chatRepo.findById(chatId);
        var user = userRepo.findById(userId);

        if (chat.isEmpty() || user.isEmpty())
            return false;

        chat.get().getMembers().add(user.get());
        return true;
    }

    public boolean addMemberByInvite(String invite, UUID userId) {
        var chat = chatRepo.findFirstByInvite(invite);
        var user = userRepo.findById(userId);

        if (chat.isEmpty() || user.isEmpty())
            return false;

        chat.get().getMembers().add(user.get());
        return true;
    }

    public boolean removeMember(UUID chatId, UUID userId) {
        var optionalChat = chatRepo.findById(chatId);
        optionalChat.ifPresent(chat ->
                {
                    chat.getMembers().removeIf(user -> user.getUserId() == userId);
                    chatRepo.save(chat);
                });
        return optionalChat.isPresent();
    }

    public UUID createChat(String name) {
        var chat = new Chat();
        chat.setName(name);
        chat.setInvite(RandomStringUtils.randomAlphanumeric(Chat.INVITE_STRING_LENGTH));
        return chatRepo.save(chat).getChatId();
    }
}
