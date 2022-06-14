package com.example.filrouge.mapper;

import com.example.filrouge.dto.PostRequest;
import com.example.filrouge.dto.PostResponse;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.Subreddit;
import com.example.filrouge.model.User;
import com.example.filrouge.repository.CommentRepository;
import com.example.filrouge.repository.VoteRepository;
import com.example.filrouge.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    // if the source and target names are identical, mapStruct will automatically map it

    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
    // mapping from post request to post object - but we also need the subreddit and user details


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post); // mapping from post object to post response

    Integer commentCount(Post post){
        return commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreationDate().toEpochMilli());
    }
}
