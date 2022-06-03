package com.example.filrouge.controller;

import com.example.filrouge.dto.RegisterRequest;
import com.example.filrouge.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@RequestBody RegisterRequest registerRequest){

        authService.singUp(registerRequest);

        return new ResponseEntity<>("User registration is successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){

        authService.verifyAccount(token);

        return new ResponseEntity<String>("This account has been activated successfully", HttpStatus.OK);
    }


}
