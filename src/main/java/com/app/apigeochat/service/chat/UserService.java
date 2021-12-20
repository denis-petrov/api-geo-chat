package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.user.Role;
import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    public final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
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
        user.setPassword(password);
        user.setRole(Role.User);
        return Optional.of(userRepo.save(user).getUserId());
    }

    public boolean addFriend(UUID userId, UUID friendId) {
        if (!userRepo.existsById(userId) || !userRepo.existsById(friendId)) return false;

        User user = userRepo.getById(userId);
        User friend = userRepo.getById(friendId);
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userRepo.save(user);
        userRepo.save(friend);

        return true;
    }

    public boolean removeFriend(UUID userId, UUID friendId) {
        if (!userRepo.existsById(userId) || !userRepo.existsById(friendId)) return false;

        User user = userRepo.getById(userId);
        User friend = userRepo.getById(friendId);
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepo.save(user);
        userRepo.save(friend);

        return true;
    }

    public void remove(UUID id) {
        userRepo.deleteById(id);
    }
}
