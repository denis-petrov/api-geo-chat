package com.app.apigeochat.controller.chat;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.dto.ChatProvidingDto;
import com.app.apigeochat.service.chat.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public UUID createChat(@RequestParam("name") String name) {
        return chatService.createChat(name);
    }

    @GetMapping("/get")
    public ChatProvidingDto getChat(@RequestParam("chatId") UUID chatId) {
        return new ChatProvidingDto(chatService.getChat(chatId));
    }

    @GetMapping("/getAllForUser")
    public List<ChatProvidingDto> getAllChatsForUser(@RequestParam("userId") UUID userId) {
        return chatService.getAllForUser(userId).stream().map(ChatProvidingDto::new).collect(Collectors.toList());
    }

    @PostMapping("/updateName")
    public void updateName(
            @RequestParam("chatId") UUID chatId,
            @RequestParam("name") String newName
    ) {
        chatService.updateName(chatId, newName);
    }

    @PostMapping("/remove")
    public void remove(@RequestParam("chatId") UUID chatId) {
        chatService.remove(chatId);
    }

    @PostMapping("/addMember")
    public void addMember(
            @RequestParam("chatId") UUID chatId,
            @RequestParam("userId") UUID userId
    ) {
        chatService.addMember(chatId, userId);
    }

    @PostMapping("/removeMember")
    public void removeMember(
            @RequestParam("chatId") UUID chatId,
            @RequestParam("userId") UUID userId
    ) {
        chatService.removeMember(chatId, userId);
    }
}
