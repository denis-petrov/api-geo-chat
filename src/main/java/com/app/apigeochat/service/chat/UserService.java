package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public interface UserService {
    User get(UUID id);

    User get(String name);

    UUID create(String username, String email, String password);

    void remove(UUID id);
}
