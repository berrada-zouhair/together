package com.together.controller;

import com.together.domain.User;
import com.together.exception.UserNotExistingException;
import com.together.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        User createdUser = userService.create(user);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{userId}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{userId}")
    public User searchUser(@PathVariable Long userId) {
        User user = userService.get(userId);
        if (user == null) {
            throw new UserNotExistingException(userId);
        }
        return user;
    }
}
