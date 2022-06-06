package com.example.filrouge.mapper;

import com.example.filrouge.dto.PostRequest;
import com.example.filrouge.dto.PostResponse;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.Subreddit;
import com.example.filrouge.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    // if the source and target names are identical, mapStruct will automatically map it

    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
    // mapping from post request to post object - but we also need the subreddit and user details


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    public abstract PostResponse mapToDto(Post post); // mapping from post object to post response
}
