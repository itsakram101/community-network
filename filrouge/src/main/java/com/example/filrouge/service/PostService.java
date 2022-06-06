package com.example.filrouge.service;

import com.example.filrouge.dto.PostRequest;
import com.example.filrouge.dto.PostResponse;
import com.example.filrouge.exception.PostNotFoundException;
import com.example.filrouge.exception.SubredditNotFoundException;
import com.example.filrouge.mapper.PostMapper;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.Subreddit;
import com.example.filrouge.model.User;
import com.example.filrouge.repository.PostRepository;
import com.example.filrouge.repository.SubredditRepository;
import com.example.filrouge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(@NotNull PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubRedditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubRedditName()));
        postRepository.save(postMapper.map(postRequest,subreddit,authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException(id.toString()));
        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPostsByUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());

    }
}
