package com.example.filrouge.controller;

import com.example.filrouge.dto.AuthenticationResponse;
import com.example.filrouge.dto.LoginRequest;
import com.example.filrouge.dto.RefreshTokenRequest;
import com.example.filrouge.dto.RegisterRequest;
import com.example.filrouge.service.AuthService;
import com.example.filrouge.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@RequestBody RegisterRequest registerRequest){

        authService.singUp(registerRequest);

        return new ResponseEntity<>("User registration is successful", HttpStatus.OK);

    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){

        authService.verifyAccount(token);
        return new ResponseEntity<>("This account has been activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){

        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest){

        return authService.refreshTokens(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> lougout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .body("refresh token deleted!");
    }
}
