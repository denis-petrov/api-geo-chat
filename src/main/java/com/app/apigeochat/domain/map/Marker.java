package com.app.apigeochat.domain.map;

import com.app.apigeochat.domain.chat.Chat;
import com.app.apigeochat.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "marker", indexes = {
        @Index(name = "lat_index", columnList = "lat"),
        @Index(name = "lng_index", columnList = "lng"),
})
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "marker_id", insertable = false, updatable = false, nullable = false)
    private UUID markerId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column()
    private String title;

    @Column()
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
