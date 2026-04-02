package com.example.financedashboard.config;

import com.example.financedashboard.entity.User;
import com.example.financedashboard.enums.Role;
import com.example.financedashboard.enums.UserStatus;
import com.example.financedashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
        }

        // Create analyst user if not exists
        if (!userRepository.existsByEmail("analyst@example.com")) {
            User analyst = new User();
            analyst.setName("Analyst User");
            analyst.setEmail("analyst@example.com");
            analyst.setPassword(passwordEncoder.encode("analyst123"));
            analyst.setRole(Role.ANALYST);
            analyst.setStatus(UserStatus.ACTIVE);
            userRepository.save(analyst);
        }

        // Create viewer user if not exists
        if (!userRepository.existsByEmail("viewer@example.com")) {
            User viewer = new User();
            viewer.setName("Viewer User");
            viewer.setEmail("viewer@example.com");
            viewer.setPassword(passwordEncoder.encode("viewer123"));
            viewer.setRole(Role.VIEWER);
            viewer.setStatus(UserStatus.ACTIVE);
            userRepository.save(viewer);
        }
    }
}