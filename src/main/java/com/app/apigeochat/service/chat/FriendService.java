package com.app.apigeochat.service.chat;

import com.app.apigeochat.domain.user.User;
import com.app.apigeochat.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
public class FriendService {
    public final UserRepository userRepo;

    @Autowired
    public FriendService(UserRepository userRepository) {
        this.userRepo = userRepository;
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
}