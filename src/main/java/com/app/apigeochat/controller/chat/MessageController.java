package com.app.apigeochat.controller.chat;

import com.app.apigeochat.controller.map.MarkerController;
import com.app.apigeochat.dto.chat.MessageProvidingDto;
import com.app.apigeochat.service.chat.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {
    public final MessageService messageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerController.class);
    private static final String CREATE_MESSAGE_LOG_MESSAGE = "New message was created: {}";
    private static final String REMOVE_MESSAGE_LOG_MESSAGE = "Message was removed: {}";

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(
            @RequestParam("senderId") String senderId,
            @RequestParam("chatId") String chatId,
            @RequestParam("message") String message,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam(value = "attachments[]") String[] attachments
    ) {
        Optional<UUID> createdMessageUuid = messageService.create(
                UUID.fromString(senderId),
                UUID.fromString(chatId),
                message,
                new Date(timestamp),
                Set.of(attachments));
        return createdMessageUuid
                .map(messageId -> {
                    LOGGER.info(CREATE_MESSAGE_LOG_MESSAGE, messageId);
                    return ResponseEntity.ok(messageId);
                })
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
        final var messages = messageService
                .getBefore(UUID.fromString(chatId), numberOfMessages, new Date(timestamp))
                .stream()
                .map(MessageProvidingDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("messageId") String messageId) {
        messageService.remove(UUID.fromString(messageId));
        LOGGER.info(REMOVE_MESSAGE_LOG_MESSAGE, messageId);
        return ResponseEntity.ok().build();
    }
}
