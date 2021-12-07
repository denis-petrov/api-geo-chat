package com.app.apigeochat.controller.chat;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.dto.ChatProvidingDto;
import com.app.apigeochat.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<UUID> createChat(@RequestParam("name") String name) {
        return ResponseEntity.ok(chatService.createChat(name));
    }

    @GetMapping("/get")
    public ResponseEntity<ChatProvidingDto> getChat(@RequestParam("chatId") String chatId) {
        Optional<Chat> chat = chatService.getChat(UUID.fromString(chatId));
        return chat.map(value -> ResponseEntity.ok(new ChatProvidingDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAllForUser")
    public ResponseEntity<List<ChatProvidingDto>> getAllChatsForUser(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(
                chatService.getAllForUser(UUID.fromString(userId))
                        .stream().map(ChatProvidingDto::new)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/updateName")
    public ResponseEntity<Void> updateName(
            @RequestParam("chatId") String chatId,
            @RequestParam("name") String newName
    ) {
        if (chatService.updateName(UUID.fromString(chatId), newName)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("chatId") String chatId) {
        chatService.remove(UUID.fromString(chatId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addMember")
    public ResponseEntity<Void> addMember(
            @RequestParam("chatId") String chatId,
            @RequestParam("userId") String userId
    ) {
        if (chatService.addMember(UUID.fromString(chatId), UUID.fromString(userId))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/removeMember")
    public ResponseEntity<Void> removeMember(
            @RequestParam("chatId") String chatId,
            @RequestParam("userId") String userId
    ) {
        if (chatService.removeMember(UUID.fromString(chatId), UUID.fromString(userId))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}
