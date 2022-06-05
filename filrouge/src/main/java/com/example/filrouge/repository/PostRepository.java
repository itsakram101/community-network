package com.example.filrouge.repository;

import com.example.filrouge.model.Post;
import com.example.filrouge.model.Subreddit;
import com.example.filrouge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
