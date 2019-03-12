package com.together.service;

import com.together.domain.Comment;
import com.together.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;

    public Long add(Comment comment) {
        return commentRepository.save(comment).getId();
    }
}
