package com.example.filrouge.service;

import com.example.filrouge.dto.VoteDto;
import com.example.filrouge.exception.PostNotFoundException;
import com.example.filrouge.exception.SpringRedditException;
import com.example.filrouge.model.Post;
import com.example.filrouge.model.Vote;
import com.example.filrouge.repository.PostRepository;
import com.example.filrouge.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.filrouge.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {

        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(voteDto.getPostId().toString()));

        // getting the latest vote by the current user for a particular post
        Optional<Vote> voteByPostAndUser = (voteRepository.
                findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser()));

        // same user can't do more than one action
        if (voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())
            && voteByPostAndUser.isPresent())
        {
            throw new SpringRedditException("you have already "+ voteDto.getVoteType());
        }

        // score system
        if (UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        }else{
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToDto(voteDto, post));
        postRepository.save(post);

    }

    public Vote mapToDto(VoteDto voteDto, Post post){
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
