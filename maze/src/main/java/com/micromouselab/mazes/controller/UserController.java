package com.micromouselab.mazes.controller;

import com.micromouselab.mazes.domain.Role;
import com.micromouselab.mazes.domain.UserDTO;
import com.micromouselab.mazes.domain.UserRoleUpdateDTO;
import com.micromouselab.mazes.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public Iterable<UserDTO> findAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDTO findById(@PathVariable Long userId){
        return userService.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content Not Found"));
    }

    @GetMapping("/by-username/{username}")
    public UserDTO findByUsername(@PathVariable String username){
        return userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!"));
    }

    @GetMapping("/by-role/{role}")
    public Iterable<UserDTO> findByRole(@PathVariable Role role){
        return userService.findByRole(role);
    }

    @PutMapping("/{userId}/role")
    public UserDTO updateRole(@PathVariable Long userId, @RequestBody UserRoleUpdateDTO userRolePutDTO) {
        return this.userService.updateUserRole(userId, userRolePutDTO.role());
    }

}
