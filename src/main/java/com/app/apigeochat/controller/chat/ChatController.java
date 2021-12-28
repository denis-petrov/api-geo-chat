package com.app.apigeochat.controller.chat;

import com.app.apigeochat.controller.map.MarkerController;
import com.app.apigeochat.dto.chat.ChatProvidingDto;
import com.app.apigeochat.service.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerController.class);
    private static final String CREATE_CHAT_LOG_MESSAGE = "New chat was created: {}";
    private static final String REMOVE_CHAT_LOG_MESSAGE = "Chat was removed: {}";

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createChat(@RequestParam("name") String name) {
        UUID chatId = chatService.createChat(name);
        LOGGER.info(CREATE_CHAT_LOG_MESSAGE, chatId);
        return ResponseEntity.ok(chatId);
    }

    @GetMapping("/get")
    public ResponseEntity<ChatProvidingDto> getChat(@RequestParam("chatId") String chatId) {
        final var chat = chatService.getChat(UUID.fromString(chatId));
        if (chat.isPresent()) {
            return ResponseEntity.ok(new ChatProvidingDto(chat.get()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }
    }

    @GetMapping("/getAllForUser")
    public ResponseEntity<List<ChatProvidingDto>> getAllChatsForUser(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(
                chatService.getAllForUser(UUID.fromString(userId))
                        .stream().map(ChatProvidingDto::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/getInvite")
    public ResponseEntity<String> getInvite(@RequestParam("chatId") String chatId) {
        final var chat = chatService.getChat(UUID.fromString(chatId));
        if (chat.isPresent()) {
            return ResponseEntity.ok(chat.get().getInvite());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }
    }

    @PostMapping("/updateName")
    public ResponseEntity<Void> updateName(
            @RequestParam("chatId") String chatId,
            @RequestParam("name") String newName
    ) {
        if (chatService.updateName(UUID.fromString(chatId), newName)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while updating chat name");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam("chatId") String chatId) {
        chatService.remove(UUID.fromString(chatId));
        LOGGER.info(REMOVE_CHAT_LOG_MESSAGE, chatId);
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while adding member to chat");
        }
    }

    @PostMapping("/addMemberByInvite")
    public ResponseEntity<Void> addByInvite(
            @RequestParam("inviteToken") String inviteToken,
            @RequestParam("userId") String userId
    ) {
        if (chatService.addMemberByInvite(inviteToken, UUID.fromString(userId))) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while adding member to chat by invite");
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while removing member to chat");
        }
    }
}
