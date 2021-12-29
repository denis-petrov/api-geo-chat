package com.app.apigeochat.repository.map;

import com.app.apigeochat.domain.map.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MarkerRepository extends JpaRepository<Marker, UUID> {
    List<Marker> findByLatBetweenAndLngBetween(Double latMin, Double latMax, Double lngMin, Double lngMax);
}