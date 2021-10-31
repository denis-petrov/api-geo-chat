package com.app.apigeochat.controller.map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/map")
public class MapController {

    @GetMapping("/markers")
    String markers() {
        return "{ {\"result 1\": \"test GET mapping 1\"}, {\"result 2\": \"test GET mapping 2\"}, {\"result 3\": \"test GET mapping 3\"} }";
    }

    @PostMapping("/markers")
    String addMarker(@RequestBody String request) {
        return "{ \"request\":\"" + request + "\", \"result\": \"request was saved\" }";
    }
}
