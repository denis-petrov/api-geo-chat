package com.app.apigeochat.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", insertable = false, updatable = false, nullable = false)
    private UUID userId;

    @ElementCollection
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "name", nullable = false, unique = true)
    @Size(max = 500, message = "User name is too long")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Size(max = 500, message = "User email is too long")
    private String email;

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    @JoinTable(
            name = "friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_user_id")
    )
    private Set<User> friends = new HashSet<>();

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    @JoinTable(
            name = "invite",
            joinColumns = @JoinColumn(name = "invited_user_id"),
            inverseJoinColumns = @JoinColumn(name = "inviting_user_id")
    )
    private Set<User> invites = new HashSet<>();

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
}
