package com.app.apigeochat.service.chat.notification.impl;

import com.app.apigeochat.service.chat.notification.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifierImpl implements Notifier {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotifierImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public <T> void sendNotifications(T sentObject, List<String> endpoints) {
        endpoints.forEach(endpoint -> simpMessagingTemplate.convertAndSend(endpoint, sentObject));
    }
}
