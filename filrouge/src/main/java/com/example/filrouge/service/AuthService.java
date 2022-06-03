package com.example.filrouge.service;

import com.example.filrouge.dto.LoginRequest;
import com.example.filrouge.dto.RegisterRequest;
import com.example.filrouge.exception.SpringRedditException;
import com.example.filrouge.model.NotifEmail;
import com.example.filrouge.model.User;
import com.example.filrouge.model.VerificationToken;
import com.example.filrouge.repository.UserRepository;
import com.example.filrouge.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

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

    @Transactional
    public void getUserAndEnable(VerificationToken verificationToken) {

        String username = verificationToken.getUser().getUsername();

       User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with" +
                " name " + username));
       user.setEnabled(true);
       userRepository.save(user);
    }

    public void login(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
    }
}
