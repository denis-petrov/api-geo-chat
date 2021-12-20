package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public interface UserService {
    User getById(UUID id);
    User getByName(String name);
    User getByEmail(String email);

    UUID create(String username, String email, String password);

    void remove(UUID id);
}
