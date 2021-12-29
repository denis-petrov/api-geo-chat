package com.app.apigeochat.service.map;

import com.app.apigeochat.domain.map.Marker;
import com.app.apigeochat.repository.map.MarkerRepository;
import com.app.apigeochat.repository.user.UserRepository;
import com.app.apigeochat.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MapService {

    public final MarkerRepository markerRepo;
    private final UserRepository userRepo;
    private final ChatService chatService;

    @Autowired
    public MapService(
            MarkerRepository markerRepo,
            UserRepository userRepo,
            ChatService chatService) {
        this.markerRepo = markerRepo;
        this.userRepo = userRepo;
        this.chatService = chatService;
    }

    public List<Marker> getMarkers(UUID senderId) {
        return markerRepo.findAll();
    }

    public Marker createMarker(
            UUID senderId,
            Double lat, Double lng,
            String title,
            String description,
            Integer chatState
    ) {
        var marker = new Marker();
        marker.setLat(lat);
        marker.setLng(lng);

        var owner = userRepo.findById(senderId);
        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Not valid senderId: " + senderId);
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title can't be empty");
        }

        marker.setOwner(owner.get());
        marker.setTitle(title);
        marker.setDescription(description);

        if (chatState == 0) {
            var newChatId = chatService.createChat(title, senderId);
            chatService.addMember(newChatId.get(), senderId);
            var newChat = chatService.getChat(newChatId.get());
            newChat.ifPresent(marker::setChat);
        }

        return markerRepo.save(marker);
    }
}
