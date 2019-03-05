package com.together.controller;

import com.together.domain.Event;
import com.together.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody Event event) {
        Long eventId = eventService.add(event);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{eventId}").buildAndExpand(eventId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable Long eventId) {
        return eventService.get(eventId);
    }
}
