package com.app.apigeochat.controller.chat;

import com.app.apigeochat.dto.MessageProvidingDto;
import com.app.apigeochat.service.chat.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {
    public final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam("senderId") String senderId,
            @RequestParam("chatId") String chatId,
            @RequestParam("message") String message,
            @RequestParam("timestamp") Long timestamp
    ) {
        Optional<UUID> createdMessageUuid = messageService.create(
                UUID.fromString(senderId), UUID.fromString(chatId), message, new Date(timestamp));
        return createdMessageUuid
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getLast")
    public ResponseEntity<List<MessageProvidingDto>> getLast(
            @RequestParam("chatId") String chatId,
            @RequestParam("numberOfMessages") int numberOfMessages
    ) {
        List<MessageProvidingDto> messages = messageService
                .getLast(UUID.fromString(chatId), numberOfMessages).stream()
                .map(MessageProvidingDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/getBefore")
    public ResponseEntity<List<MessageProvidingDto>> getBefore(
            @RequestParam("chatId") String chatId,
            @RequestParam("numberOfMessages") int numberOfMessages,
            @RequestParam("timestamp") Long timestamp
    ) {
        List<MessageProvidingDto> messages = messageService
                .getBefore(UUID.fromString(chatId), numberOfMessages, new Date(timestamp))
                .stream().map(MessageProvidingDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("messageId") String messageId) {
        messageService.remove(UUID.fromString(messageId));
        return ResponseEntity.ok().build();
    }
}
