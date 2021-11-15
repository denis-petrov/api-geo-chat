package com.app.apigeochat.service.chat.notification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Notifier {
    <T> void sendNotifications(T sentObject, List<String> endpoints);
}
