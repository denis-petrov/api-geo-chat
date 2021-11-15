package com.app.apigeochat.dto;

import com.app.apigeochat.domain.Chat;
import com.app.apigeochat.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatProvidingDto {
    private UUID chatId;
    private List<UUID> members;
    private String name;

    public ChatProvidingDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.name = chat.getName();
        this.members = chat.getMembers().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
    }
}
