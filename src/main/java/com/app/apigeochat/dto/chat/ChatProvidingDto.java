package com.app.apigeochat.dto.chat;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatProvidingDto {
    private UUID chatId;
    private UUID adminId;
    private String name;
    private List<UUID> members;

    public ChatProvidingDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.adminId = chat.getAdmin().getUserId();
        this.name = chat.getName();
        this.members = chat.getMembers().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
    }
}
