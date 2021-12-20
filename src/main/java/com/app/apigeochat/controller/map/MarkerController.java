package com.app.apigeochat.controller.map;

import com.app.apigeochat.domain.map.Marker;
import com.app.apigeochat.dto.map.MarkerCreationDto;
import com.app.apigeochat.service.map.MapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/map/markers")
public class MarkerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerController.class);
    private static final String NEW_MARKER_LOG = "New marker has been created, id: {}";

    public final MapService mapService;

    @Autowired
    public MarkerController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping
    public ResponseEntity<List<Marker>> markersByPosition(
            @RequestParam("senderId") String senderId
    ) {
        final var markers = mapService.getMarkers(UUID.fromString(senderId));
        return ResponseEntity.ok(markers);
    }

    @PostMapping
    public ResponseEntity<Marker> createMarker(@RequestBody MarkerCreationDto markerCreationDto) {
        final var createdMarker = mapService.createMarker(
                markerCreationDto.getSenderId(),
                markerCreationDto.getLat(),
                markerCreationDto.getLng(),
                markerCreationDto.getMarkerName(),
                markerCreationDto.getMarkerDescription(),
                markerCreationDto.getChatState()
        );
        LOGGER.info(NEW_MARKER_LOG, createdMarker.toString());
        return ResponseEntity.ok(createdMarker);
    }
}
