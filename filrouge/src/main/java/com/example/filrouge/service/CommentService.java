package com.example.filrouge.service;


import com.example.filrouge.dto.CommentDto;
import com.example.filrouge.exception.PostNotFoundException;
import com.example.filrouge.mapper.CommentMapper;
import com.example.filrouge.model.Comment;
import com.example.filrouge.model.NotifEmail;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.User;
import com.example.filrouge.repository.CommentRepository;
import com.example.filrouge.repository.PostRepository;
import com.example.filrouge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    private static final String POST_URL = "";


    public void createComment(CommentDto commentDto){

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getUsername().toString()));
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(comment.getUser().getUsername() +
                " posted a comment on your post" + POST_URL);
        sendCommentNotification(message,post.getUser(), comment);
    }

    private void sendCommentNotification(String message, User user, Comment comment) {
        mailService.sendMail(new NotifEmail(comment.getUser().getUsername() +
                " made a comment on your post", user.getEmail(), message));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public List<CommentDto> getCommentsByUsername(String userName) {

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return commentRepository.findByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());

    }
}
