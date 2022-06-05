package com.example.filrouge.controller;

import com.example.filrouge.dto.SubRedditDto;
import com.example.filrouge.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubreddit(@RequestBody SubRedditDto subRedditDto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subRedditService.save(subRedditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subRedditService.retrieveAllSubReddits());
    }
}
