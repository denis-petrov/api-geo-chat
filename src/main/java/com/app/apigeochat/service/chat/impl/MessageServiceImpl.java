package com.app.apigeochat.service.chat.impl;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.Message;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.ChatRepository;
import com.app.apigeochat.repository.MessageRepository;
import com.app.apigeochat.repository.UserRepository;
import com.app.apigeochat.service.chat.MessageService;
import com.app.apigeochat.service.chat.notification.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepo;
    private final ChatRepository chatRepo;
    private final UserRepository userRepo;
    private final Notifier notifier;

    @Autowired
    public MessageServiceImpl(
            MessageRepository messageRepo,
            ChatRepository chatRepo,
            UserRepository userRepo,
            Notifier notifier) {
        this.messageRepo = messageRepo;
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.notifier = notifier;
    }

    @Override
    public UUID create(UUID senderId, UUID chatId, String text, Date sentDate) {
        Message message = new Message();

        User sender = userRepo.getById(senderId);
        Chat chat = chatRepo.getById(chatId);

        message.setMessage(text);
        message.setChat(chat);
        message.setSender(sender);
        message.setSentDate(sentDate);

        messageRepo.save(message);
        notifier.sendNotifications(message.getMessageId(), getEndpoints(chat.getMembers(), "/create/%s"));

        return message.getMessageId();
    }

    @Override
    public void remove(UUID messageId) {
        Message message = messageRepo.getById(messageId);
        Chat chat = message.getChat();

        messageRepo.deleteById(messageId);
        notifier.sendNotifications(messageId, getEndpoints(chat.getMembers(), "/remove/%s"));
    }

    @Override
    public List<Message> getLast(UUID chatId, int numberOfMessages) {
        Pageable pageable = PageRequest.of(0, numberOfMessages);
        Chat chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesByChatEqualsOrderBySentDateDesc(chat, pageable);
    }

    @Override
    public List<Message> getBefore(UUID chatId, int numberOfMessages, Date date) {
        Pageable pageable = PageRequest.of(0, numberOfMessages);
        Chat chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesBySentDateLessThanAndChatEqualsOrderBySentDateDesc(date, chat, pageable);
    }

    private List<String> getEndpoints(Collection<User> users, String endpointFormat) {
        return users.stream()
                .map(user -> String.format(endpointFormat, user.getUserId()))
                .collect(Collectors.toList());
    }
}
