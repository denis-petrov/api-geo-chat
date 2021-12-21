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

    public boolean isFriends(UUID firstUserId, UUID secondUserId) {
        User firstUser = userRepo.getById(firstUserId);
        User secondUser = userRepo.getById(secondUserId);

        return firstUser.getFriends().contains(secondUser) ||
                secondUser.getFriends().contains(firstUser);
    }

    public boolean inviteToFriends(UUID invitedUserId, UUID invitingUserId) {
        if (!userRepo.existsById(invitedUserId) || !userRepo.existsById(invitingUserId)) return false;

        User invitedUser = userRepo.getById(invitedUserId);
        User invitingUser = userRepo.getById(invitingUserId);
        invitedUser.getInvites().add(invitingUser);

        userRepo.save(invitedUser);

        return true;
    }

    public boolean acceptInvite(UUID invitedUserId, UUID invitingUserId) {
        if (!userRepo.existsById(invitedUserId) || !userRepo.existsById(invitingUserId)) return false;

        User invitedUser = userRepo.getById(invitedUserId);
        User invitingUser = userRepo.getById(invitingUserId);
        invitingUser.getFriends().add(invitedUser);
        invitedUser.getFriends().add(invitingUser);

        invitedUser.getInvites().remove(invitingUser);

        userRepo.save(invitedUser);
        userRepo.save(invitingUser);

        return true;
    }

    public boolean rejectInvite(UUID invitedUserId, UUID invitingUserId) {
        if (!userRepo.existsById(invitedUserId) || !userRepo.existsById(invitingUserId)) return false;

        User invitedUser = userRepo.getById(invitedUserId);
        User invitingUser = userRepo.getById(invitingUserId);

        invitedUser.getInvites().remove(invitingUser);

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
