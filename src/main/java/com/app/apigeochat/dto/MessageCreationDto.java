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
    private UUID senderId;
    private UUID chatId;
    private String message;
    private Date sentDate;
}
