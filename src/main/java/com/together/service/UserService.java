package com.together.service;

import com.together.domain.User;
import com.together.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User get(Long userId) {
        return userRepository.findById(userId);
    }
}
