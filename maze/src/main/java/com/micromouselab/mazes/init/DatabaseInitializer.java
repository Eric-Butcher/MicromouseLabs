package com.micromouselab.mazes.init;

import com.micromouselab.mazes.domain.RegisterDTO;
import com.micromouselab.mazes.domain.Role;
import com.micromouselab.mazes.domain.User;
import com.micromouselab.mazes.repository.UserRepository;
import com.micromouselab.mazes.security.MazeUserDetailsService;
import com.micromouselab.mazes.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {

    @Value("${adminuser.username}")
    private String adminUsername;

    @Value("${adminuser.password}")
    private String adminUserPassword;

    private final UserRepository userRepository;
    private final AuthService authService;

    public DatabaseInitializer(UserRepository userRepository, AuthService authService){
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void run(String... args){
        Optional<User> user = userRepository.findByUsername(adminUsername);
        if (!user.isPresent()){
            RegisterDTO registerDTO = new RegisterDTO(adminUsername, adminUserPassword);
            authService.registerUser(registerDTO);
            Optional<User> adminUser = userRepository.findByUsername(adminUsername);
            if (adminUser.isPresent()){
                adminUser.get().setRole(Role.ADMIN);
                userRepository.save(adminUser.get());
            }
        }
    }
}
