package com.example.filrouge.repository;

import com.example.filrouge.model.Post;
import com.example.filrouge.model.User;
import com.example.filrouge.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // getting the latest vote by the user for a certain post
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
