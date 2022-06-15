package com.example.filrouge.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
    private String username;
}
