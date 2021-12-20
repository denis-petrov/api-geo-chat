package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.chat.Message;
import com.app.apigeochat.repository.chat.ChatRepository;
import com.app.apigeochat.repository.chat.MessageRepository;
import com.app.apigeochat.repository.user.UserRepository;
import com.app.apigeochat.service.chat.notification.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepo;
    private final ChatRepository chatRepo;
    private final UserRepository userRepo;
    private final Notifier notifier;

    private static final String MESSAGE_NOTIFICATION_BASE_ENDPOINT = "/queue/message";
    private static final String MESSAGE_NOTIFICATION_CREATE_ENDPOINT = MESSAGE_NOTIFICATION_BASE_ENDPOINT + "/create";
    private static final String MESSAGE_NOTIFICATION_REMOVE_ENDPOINT = MESSAGE_NOTIFICATION_BASE_ENDPOINT + "/create";

    @Autowired
    public MessageService(
            MessageRepository messageRepo,
            ChatRepository chatRepo,
            UserRepository userRepo,
            Notifier notifier
    ) {
        this.messageRepo = messageRepo;
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.notifier = notifier;
    }

    public Optional<UUID> create(UUID senderId, UUID chatId, String text, Date sentDate,
                                 Set<String> attachments) {
        var message = new Message();

        var sender = userRepo.findById(senderId);
        var chat = chatRepo.findById(chatId);

        if (sender.isEmpty() || chat.isEmpty())
            return Optional.empty();

        message.setMessage(text);
        message.setChat(chat.get());
        message.setSender(sender.get());
        message.setSentDate(sentDate);
        message.setAttachments(attachments);

        messageRepo.save(message);
        notifier.sendNotifications(chat.get().getChatId(),
                MESSAGE_NOTIFICATION_CREATE_ENDPOINT, getMemberIds(chat.get()));

        return Optional.ofNullable(message.getMessageId());
    }

    public void remove(UUID messageId) {
        var message = messageRepo.findById(messageId);
        if (message.isEmpty()) return;

        Chat chat = message.get().getChat();
        messageRepo.deleteById(messageId);
        notifier.sendNotifications(chat.getChatId(),
                MESSAGE_NOTIFICATION_REMOVE_ENDPOINT, getMemberIds(chat));
    }

    public List<Message> getLast(UUID chatId, int numberOfMessages) {
        var pageable = PageRequest.of(0, numberOfMessages);
        var chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesByChatEqualsOrderBySentDateDesc(chat, pageable);
    }

    public List<Message> getBefore(UUID chatId, int numberOfMessages, Date date) {
        var pageable = PageRequest.of(0, numberOfMessages);
        var chat = chatRepo.getById(chatId);
        return messageRepo.findMessagesBySentDateLessThanAndChatEqualsOrderBySentDateDesc(date, chat, pageable);
    }

    private List<String> getMemberIds(Chat chat) {
        return chat.getMembers().stream()
                .map(user -> user.getUserId().toString()).collect(Collectors.toList());
    }
}
