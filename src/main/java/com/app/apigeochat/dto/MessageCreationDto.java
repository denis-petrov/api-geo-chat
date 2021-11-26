package com.app.apigeochat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class MessageCreationDto {
    private String senderId;
    private String chatId;
    private String message;
    private Date sentDate;
}
