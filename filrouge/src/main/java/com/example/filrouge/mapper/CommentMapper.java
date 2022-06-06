package com.example.filrouge.mapper;

import com.example.filrouge.dto.CommentDto;
import com.example.filrouge.model.Comment;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    public abstract Comment map(CommentDto commentDto, Post post, User user);


    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    public abstract CommentDto mapToDto(Comment comment);
}
