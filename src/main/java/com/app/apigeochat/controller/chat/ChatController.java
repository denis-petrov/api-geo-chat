package com.app.apigeochat.controller.chat;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.dto.ChatProvidingDto;
import com.app.apigeochat.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public UUID createChat(@RequestParam("name") String name) {
        return chatService.createChat(name);
    }

    @GetMapping("/get")
    public ChatProvidingDto getChat(@RequestParam("chatId") String chatId) {
        return new ChatProvidingDto(chatService.getChat(UUID.fromString(chatId)));
    }

    @GetMapping("/getAllForUser")
    public List<ChatProvidingDto> getAllChatsForUser(@RequestParam("userId") String userId) {
        return chatService.getAllForUser(UUID.fromString(userId))
                .stream().map(ChatProvidingDto::new).collect(Collectors.toList());
    }

    @PostMapping("/updateName")
    public void updateName(
            @RequestParam("chatId") String chatId,
            @RequestParam("name") String newName
    ) {
        chatService.updateName(UUID.fromString(chatId), newName);
    }

    @PostMapping("/remove")
    public void remove(@RequestParam("chatId") String chatId) {
        chatService.remove(UUID.fromString(chatId));
    }

    @PostMapping("/addMember")
    public void addMember(
            @RequestParam("chatId") String chatId,
            @RequestParam("userId") String userId
    ) {
        chatService.addMember(UUID.fromString(chatId), UUID.fromString(userId));
    }

    @PostMapping("/removeMember")
    public void removeMember(
            @RequestParam("chatId") String chatId,
            @RequestParam("userId") String userId
    ) {
        chatService.removeMember(UUID.fromString(chatId), UUID.fromString(userId));
    }
}
