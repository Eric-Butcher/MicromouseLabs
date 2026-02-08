package com.micromouselab.mazes.service;

import com.micromouselab.mazes.domain.User;
import com.micromouselab.mazes.domain.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO mapToDTO(User userEntity){
        UserDTO userDTO = new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getRole());
        return userDTO;
    }

}
