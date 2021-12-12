package com.app.apigeochat.dto;

import com.app.apigeochat.domain.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class MessageProvidingDto {
    private UUID messageId;
    private UUID senderId;
    private UUID chatId;
    private String message;
    private Date sentDate;
    private Set<String> attachments;

    public MessageProvidingDto(Message message) {
        this.messageId = message.getMessageId();
        this.senderId = message.getSender().getUserId();
        this.chatId = message.getChat().getChatId();
        this.message = message.getMessage();
        this.sentDate = message.getSentDate();
        this.attachments = message.getAttachments();
    }
}
