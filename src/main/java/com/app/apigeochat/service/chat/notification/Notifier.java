package com.app.apigeochat.service.chat.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class Notifier {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public Notifier(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public <T extends Serializable> void sendNotifications(T payload, String endpoint, List<String> userIds) {
        userIds.forEach(userId -> simpMessagingTemplate.convertAndSendToUser(userId, endpoint, payload));
    }
}
