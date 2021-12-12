package com.app.apigeochat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
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

    @Column(name="message", nullable = false)
    @Size(max = 1000, message = "Message is too long")
    private String message;

    @Column(name="sent_date", nullable = false)
    private Date sentDate;

    @ElementCollection
    @CollectionTable(name = "message_attachment", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "attachment")
    @Size(max = 20, message = "Too big number of attachments")
    private Set<String> attachments;
}
