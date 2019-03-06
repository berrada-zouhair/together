package com.together.repository;

import com.together.domain.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    User findById(Long userId);
}
