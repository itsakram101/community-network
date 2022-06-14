package com.example.filrouge.repository;

import com.example.filrouge.model.Comment;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findByUser(User user);
}
