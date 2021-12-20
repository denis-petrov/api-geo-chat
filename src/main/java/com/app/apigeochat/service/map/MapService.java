package com.app.apigeochat.service.map;

import com.app.apigeochat.domain.map.Marker;
import com.app.apigeochat.repository.map.MarkerRepository;
import com.app.apigeochat.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MapService {
    public final MarkerRepository markerRepo;
    private final UserRepository userRepo;

    @Autowired
    public MapService(
            MarkerRepository markerRepo,
            UserRepository userRepo
    ) {
        this.markerRepo = markerRepo;
        this.userRepo = userRepo;
    }

    public List<Marker> getMarkers(UUID senderId) {
        return markerRepo.findAll();
    }

    public Marker createMarker(
            UUID senderId,
            Double lat, Double lng,
            String markerName, String markerDescription,
            Integer chatState
    ) {
        Marker marker = new Marker();
        marker.setLat(lat);
        marker.setLng(lng);

        var owner = userRepo.findById(senderId).get();
        marker.setOwner(owner);

        return markerRepo.save(marker);
    }
}
