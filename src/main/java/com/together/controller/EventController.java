package com.together.controller;

import com.together.domain.Event;
import com.together.domain.User;
import com.together.exception.EventNotExistingException;
import com.together.service.EventService;
import com.together.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody Event event) {
        Long eventId = eventService.add(event);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{eventId}").buildAndExpand(eventId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable Long eventId) {
        Event event = eventService.get(eventId);
        if (event == null) {
            throw new EventNotExistingException(eventId);
        }
        return event;
    }

    @PostMapping("/{eventId}/participant/{participantId}")
    public void addParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        Event event = eventService.get(eventId);
        User user = userService.get(participantId);
        event.addParticipant(user);
        eventService.add(event);
    }
}
