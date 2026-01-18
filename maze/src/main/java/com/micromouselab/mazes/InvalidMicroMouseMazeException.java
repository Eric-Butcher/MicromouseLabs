package com.micromouselab.mazes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid maze.")
public class InvalidMicroMouseMazeException extends RuntimeException {
    public InvalidMicroMouseMazeException(String notAMicroMouseMaze) {
    }
}
