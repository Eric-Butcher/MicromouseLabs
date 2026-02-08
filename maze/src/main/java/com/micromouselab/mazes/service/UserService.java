package com.micromouselab.mazes.service;

import com.micromouselab.mazes.domain.*;
import com.micromouselab.mazes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Iterable<UserDTO> findAll(){
        return this.userRepository.findAll().stream().map(UserMapper::mapToDTO).toList();
    }

    public Optional<UserDTO> findById(Long id){
        Optional<User> optionalUserEntity =  this.userRepository.findById(id);
        if (optionalUserEntity.isPresent()){
            User userEntity = optionalUserEntity.get();
            UserDTO userDTO = UserMapper.mapToDTO(userEntity);
            return Optional.of(userDTO);
        }
        return Optional.empty();
    }

    public boolean existsById(Long id){
        return this.userRepository.existsById(id);
    }

    public Optional<UserDTO> findByUsername(String username){
        Optional<User> optionalUserEntity =  this.userRepository.findByUsername(username);
        if (optionalUserEntity.isPresent()){
            User userEntity = optionalUserEntity.get();
            UserDTO userDTO = UserMapper.mapToDTO(userEntity);
            return Optional.of(userDTO);
        }
        return Optional.empty();
    }

    public Iterable<UserDTO> findByRole(Role role){

        return this.userRepository.findByRole(role).stream().map(UserMapper::mapToDTO).toList();

    }

    @Transactional
    public UserDTO updateUserRole(Long userId, Role updatedRole){

        List<Role> rolesChangeableTo = new ArrayList<Role>(List.of(Role.USER, Role.APPROVED));
        if (!rolesChangeableTo.contains(updatedRole)) {
            throw new IllegalArgumentException("Cannot change to an invalid role.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        user.setRole(updatedRole);
        User updatedUser = userRepository.save(user);
        return UserMapper.mapToDTO(updatedUser);
    }


}
