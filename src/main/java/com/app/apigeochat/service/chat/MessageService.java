package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.chat.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public interface MessageService {
    UUID create(UUID senderId, UUID chatId, String text, Date sentDate);

    void remove(UUID messageId);

    List<Message> getLast(UUID chatId, int numberOfMessages);

    List<Message> getBefore(UUID chatId, int numberOfMessages, Date date);
}
