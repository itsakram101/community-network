package com.example.filrouge.service;

import com.example.filrouge.dto.SubRedditDto;
import com.example.filrouge.mapper.SubRedditMapper;
import com.example.filrouge.model.Subreddit;
import com.example.filrouge.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {

    private final SubredditRepository subredditRepository;
    private final SubRedditMapper subRedditMapper;

    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto){
        Subreddit saved = subredditRepository.save(subRedditMapper.mapDtoToSubreddit(subRedditDto));
        subRedditDto.setId(saved.getId());
        return subRedditDto;
    }

    // private Subreddit mapSubRedditDto(SubRedditDto subRedditDto) {
    //      return Subreddit.builder().name(subRedditDto.getName())
    //          .description(subRedditDto.getDescription())
    //             .build();
    //}

    @Transactional(readOnly = true)
    public List<SubRedditDto> retrieveAllSubReddits(){
        return subredditRepository.findAll()
                .stream()
                .map(subRedditMapper::mapSubredditToDto)// whatever arg is passed, same is passed to mapToDto
                // mapper converts objects from one type to another(converting to subredditdto object)
                .collect(toList());// to accumulate it rather than print it out

    }

    // private SubRedditDto mapToDto(Subreddit subreddit) {
    //    return SubRedditDto.builder()
    //            .id(subreddit.getId())
    //            .name(subreddit.getName())
    //            .numberOfPosts(subreddit.getPosts().size())
    //            .build();
    //}
}
