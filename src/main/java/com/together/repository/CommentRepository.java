package com.together.repository;

import com.together.domain.Comment;
import org.springframework.data.repository.Repository;

public interface CommentRepository extends Repository<Comment, Long> {

    Comment save(Comment comment);
}
