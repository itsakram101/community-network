package com.example.filrouge.controller;


import com.example.filrouge.dto.PostRequest;
import com.example.filrouge.dto.PostResponse;
import com.example.filrouge.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){

        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPosts());
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPostsBySubreddit(id));
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPostsByUser(username));
    }


}
