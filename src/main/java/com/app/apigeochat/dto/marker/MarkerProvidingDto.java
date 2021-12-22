package com.app.apigeochat.dto.marker;

import com.app.apigeochat.domain.map.Marker;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MarkerProvidingDto {
    private UUID markerId;
    private UUID ownerId;
    private UUID chatId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;

    public MarkerProvidingDto(Marker marker) {
        this.markerId = marker.getMarkerId();
        this.ownerId = marker.getOwner().getUserId();
        if (marker.getChat() != null) {
            this.chatId = marker.getChat().getChatId();
        }
        this.lat = marker.getLat();
        this.lng = marker.getLng();
        this.title = marker.getTitle();
        this.description = marker.getDescription();
    }
}
