package com.example.filrouge.controller;

import com.example.filrouge.dto.CommentDto;
import com.example.filrouge.model.Comment;
import com.example.filrouge.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto){

        commentService.createComment(commentDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<CommentDto>> getCommentsFromPost(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllComments());
    }
}
