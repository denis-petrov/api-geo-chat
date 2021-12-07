package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.Message;
import com.app.apigeochat.domain.User;
import com.app.apigeochat.repository.ChatRepository;
import com.app.apigeochat.repository.MessageRepository;
import com.app.apigeochat.repository.UserRepository;
import com.app.apigeochat.service.chat.notification.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepo;
    private final ChatRepository chatRepo;
    private final UserRepository userRepo;
    private final Notifier notifier;

    @Autowired
    public MessageService(
            MessageRepository messageRepo,
            ChatRepository chatRepo,
            UserRepository userRepo,
            Notifier notifier) {
        this.messageRepo = messageRepo;
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.notifier = notifier;
    }

    public Optional<UUID> create(UUID senderId, UUID chatId, String text, Date sentDate) {
        Message message = new Message();

        Optional<User> sender = userRepo.findById(senderId);
        Optional<Chat> chat = chatRepo.findById(chatId);

        if (sender.isPresent() && chat.isPresent()) {
            message.setMessage(text);
            message.setChat(chat.get());
            message.setSender(sender.get());
            message.setSentDate(sentDate);

            messageRepo.save(message);
            notifier.sendNotifications(chat.get().getChatId(), "/queue/message/create", getMemberIds(chat.get()));

            return Optional.ofNullable(message.getMessageId());
        } else {
            return Optional.empty();
        }
    }

    public void remove(UUID messageId) {
        Optional<Message> message = messageRepo.findById(messageId);
        if (message.isPresent()) {
            Chat chat = message.get().getChat();
            messageRepo.deleteById(messageId);
            notifier.sendNotifications(chat.getChatId(), "/queue/message/remove", getMemberIds(chat));
        }
    }

    public List<Message> getLast(UUID chatId, int numberOfMessages) {
        Pageable pageable = PageRequest.of(0, numberOfMessages);
        Chat chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesByChatEqualsOrderBySentDateDesc(chat, pageable);
    }

    public List<Message> getBefore(UUID chatId, int numberOfMessages, Date date) {
        Pageable pageable = PageRequest.of(0, numberOfMessages);
        Chat chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesBySentDateLessThanAndChatEqualsOrderBySentDateDesc(date, chat, pageable);
    }

    private List<String> getMemberIds(Chat chat) {
        return chat.getMembers().stream().map(user -> user.getUserId().toString()).collect(Collectors.toList());
    }
}
