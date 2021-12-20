package com.app.apigeochat.repository.chat;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.chat.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
/*
    @Query("select Message from Message where (sentDate < :date) and (Chat.chatId = :chatId) order by sentDate")
    List<Message> findMessagesBeforeSelectedDate(
            @Param("chatId") UUID chatId,
            @Param("date") @Temporal(TemporalType.TIMESTAMP) Date date,
            Pageable pageable);

    @Query("select Message from Message where (Chat.chatId = :chatId) order by sentDate")
    List<Message> findMessagesSortedByDate(
            @Param("chatId") UUID chatId,
            Pageable pageable);

*/
    List<Message> findMessagesBySentDateLessThanAndChatEqualsOrderBySentDateDesc(Date sentDate, Chat chat, Pageable pageable);
    List<Message> findMessagesByChatEqualsOrderBySentDateDesc(Chat chat, Pageable pageable);
}