package com.app.apigeochat.dto.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MarkerCreationDto {
    private UUID senderId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;
    private Integer chatState;
}
