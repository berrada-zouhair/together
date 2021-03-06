package com.together.controller;

import com.together.domain.Activity;
import com.together.domain.Comment;
import com.together.domain.Event;
import com.together.domain.User;
import com.together.exception.EventNotExistingException;
import com.together.service.CommentService;
import com.together.service.EventService;
import com.together.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private EventService eventService;
    private UserService userService;
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody Event event, @RequestParam Long owner) {
        User user = userService.get(owner);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        event.setOwner(user);
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

    @PostMapping("/{eventId}/participant")
    public ResponseEntity<Void> addParticipant(@PathVariable Long eventId, @RequestParam Long userId) {
        Event event = eventService.get(eventId);
        User user = userService.get(userId);
        if (user == null || event == null) {
            return ResponseEntity.badRequest().build();
        }
        event.addParticipant(user);
        eventService.add(event);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/liker")
    public ResponseEntity<Void> addLiker(@PathVariable Long eventId, @RequestParam Long userId) {
        Event event = eventService.get(eventId);
        User user = userService.get(userId);
        if (event == null || user == null) {
            return ResponseEntity.badRequest().build();
        }
        event.addLiker(user);
        eventService.add(event);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/comment")
    public ResponseEntity<Void> create(@RequestBody Comment comment, @PathVariable Long eventId, @RequestParam Long owner) {
        Event storedEvent = eventService.get(eventId);
        User storedOwner = userService.get(owner);
        if (storedEvent == null || storedOwner == null) {
            return ResponseEntity.badRequest().build();
        }
        comment.setOwner(storedOwner);
        Long commentId = commentService.add(comment);
        storedEvent.addComment(comment);
        eventService.add(storedEvent);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{commentId}")
                .buildAndExpand(commentId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@RequestParam Long userId) {
        User user = userService.get(userId);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Activity> activities = user.getActivities();
        List<Event> events = eventService.get(now().withMinute(0).withSecond(0).withNano(0),
                activities.toArray(new Activity[activities.size()]));
        return ResponseEntity.ok().body(events);
    }
}
