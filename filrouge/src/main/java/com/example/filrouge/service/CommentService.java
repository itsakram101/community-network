package com.example.filrouge.service;


import com.example.filrouge.dto.CommentDto;
import com.example.filrouge.exception.PostNotFoundException;
import com.example.filrouge.mapper.CommentMapper;
import com.example.filrouge.model.Comment;
import com.example.filrouge.model.Post;
import com.example.filrouge.repository.CommentRepository;
import com.example.filrouge.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentDto commentDto;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    public void createComment(CommentDto commentDto){

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getUsername().toString()));
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllComments() {

        return commentRepository.findAll()
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
