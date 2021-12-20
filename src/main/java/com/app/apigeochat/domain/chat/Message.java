package com.app.apigeochat.domain.chat;

import com.app.apigeochat.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="message_id", insertable = false, updatable = false, nullable = false)
    private UUID messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(nullable = false)
    private String message;

    @Column(name="sent_date", nullable = false)
    private Date sentDate;
}
