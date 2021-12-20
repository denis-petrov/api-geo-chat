package com.app.apigeochat.domain.chat;

import com.app.apigeochat.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id", insertable = false, updatable = false, nullable = false)
    private UUID chatId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<User> members;

    @Column(name = "name", nullable = false)
    @Size(max = 500, message = "Chat name is too long")
    private String name;
}
