package com.example.filrouge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubRedditDto {

    private Long id;
    private String description;
    private String name;
    private Integer numberOfPosts;

}
