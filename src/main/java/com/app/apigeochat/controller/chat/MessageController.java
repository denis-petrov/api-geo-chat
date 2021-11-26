package com.app.apigeochat.controller.chat;

import com.app.apigeochat.dto.MessageCreationDto;
import com.app.apigeochat.dto.MessageProvidingDto;
import com.app.apigeochat.service.chat.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    public UUID create(@RequestBody MessageCreationDto messageDto) {
        return messageService.create(
                UUID.fromString(messageDto.getSenderId()),
                UUID.fromString(messageDto.getChatId()),
                messageDto.getMessage(),
                messageDto.getSentDate());
    }

    @GetMapping("/getLast")
    public List<MessageProvidingDto> getLast(
            @RequestParam("chatId") String chatId,
            @RequestParam("numberOfMessages") int numberOfMessages
    ) {
        return messageService.getLast(UUID.fromString(chatId), numberOfMessages).stream()
                .map(MessageProvidingDto::new).collect(Collectors.toList());
    }

    @GetMapping("/getBefore")
    public List<MessageProvidingDto> getBefore(
            @RequestParam("chatId") String chatId,
            @RequestParam("numberOfMessages") int numberOfMessages,
            @RequestParam("date") Date date
    ) {
        return messageService.getBefore(UUID.fromString(chatId), numberOfMessages, date).stream()
                .map(MessageProvidingDto::new).collect(Collectors.toList());
    }

    @PostMapping("/remove")
    public void remove(@RequestParam("chatId") String chatId) {
        messageService.remove(UUID.fromString(chatId));
    }
}
