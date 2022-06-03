package com.example.filrouge.service;

import com.example.filrouge.model.User;
import com.example.filrouge.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // retrieve the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        // if user doesn't exist, we throw exception
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("No user found with this username "
                + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(),true,
                true,true, getAuthorities("USER"));
    }

    // providing an authority
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {

        return singletonList(new SimpleGrantedAuthority(role));
    }
}
