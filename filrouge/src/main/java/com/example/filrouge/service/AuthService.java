package com.example.filrouge.service;

import com.example.filrouge.dto.AuthenticationResponse;
import com.example.filrouge.dto.LoginRequest;
import com.example.filrouge.dto.RefreshTokenRequest;
import com.example.filrouge.dto.RegisterRequest;
import com.example.filrouge.exception.SpringRedditException;
import com.example.filrouge.model.NotifEmail;
import com.example.filrouge.model.User;
import com.example.filrouge.model.VerificationToken;
import com.example.filrouge.repository.UserRepository;
import com.example.filrouge.repository.VerificationTokenRepository;
import com.example.filrouge.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void singUp(RegisterRequest registerRequest){

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotifEmail("Activate your account.", user.getEmail(), "Thank you for" +
                " signing" + " up to this Spring Reddit clone, " +
                "If you would please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
    //the number of random version-4 UUIDs which need to be generated in order to have a 50% probability of at least
        // one collision is 2.71 quintillion computed.
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {

        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        // if not exist, thrown exception
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token"));

        getUserAndEnable(verificationToken.get());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("this user name not found - " + principal.getSubject()));
    }


    @Transactional
    public void getUserAndEnable(VerificationToken verificationToken) {

        String username = verificationToken.getUser().getUsername();

       User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with" +
                " name " + username));
       user.setEnabled(true);
       userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        // set authentication object inside the security context
        // if you want to check if a user is logged in - look up the context
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String userToken = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(userToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMilliseconds()))
                .username(loginRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshTokens(RefreshTokenRequest refreshTokenRequest){

        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMilliseconds()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
