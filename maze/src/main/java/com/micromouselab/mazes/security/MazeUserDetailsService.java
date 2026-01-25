package com.micromouselab.mazes.security;

import com.micromouselab.mazes.domain.User;
import com.micromouselab.mazes.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MazeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MazeUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByUsername(username).isPresent()){
            User user =  userRepository.findByUsername(username).get();
            return new MazeUserDetails(user);
        }
        throw new UsernameNotFoundException("Username not found.");
    }
}
