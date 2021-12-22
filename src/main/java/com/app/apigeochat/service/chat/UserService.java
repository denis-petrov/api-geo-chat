package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.user.Role;
import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    public final UserRepository userRepo;
    public final EncryptionService encryptionService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            EncryptionService encryptionService
    ) {
        this.userRepo = userRepository;
        this.encryptionService = encryptionService;
    }

    public Optional<User> getById(UUID id) {
        return userRepo.findById(id);
    }

    public Optional<User> getByName(String name) {
        return userRepo.findByName(name);
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<User> searchByName(String substring) {
        return userRepo.findByNameContaining(substring);
    }

    public Optional<UUID> create(String username, String email, String password) {
        if (userRepo.existsByName(username) || userRepo.existsByEmail(email)) return Optional.empty();

        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(encryptionService.encrypt(password));
        user.setRole(Role.User);
        return Optional.of(userRepo.save(user).getUserId());
    }

    public void remove(UUID id) {
        userRepo.deleteById(id);
    }
}
