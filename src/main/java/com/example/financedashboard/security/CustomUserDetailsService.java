package com.example.financedashboard.security;

import com.example.financedashboard.entity.User;
import com.example.financedashboard.enums.UserStatus;
import com.example.financedashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("User is not active");
        }

        return UserPrincipal.create(user);
    }
}