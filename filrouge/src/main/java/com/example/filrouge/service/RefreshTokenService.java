package com.example.filrouge.service;

// class responsible for create validate delete refresh tokens

import com.example.filrouge.exception.SpringRedditException;
import com.example.filrouge.model.RefreshToken;
import com.example.filrouge.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("False refresh token!"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);

    }


}
