package com.together.controller;

import com.together.domain.Comment;
import com.together.domain.Event;
import com.together.domain.User;
import com.together.service.CommentService;
import com.together.service.EventService;
import com.together.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private EventService eventService;

    private UserService userService;

    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Comment comment, @RequestParam Long owner,
                                       @RequestParam Long event) {
        Event storedEvent = eventService.get(event);
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
}
