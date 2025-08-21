package com.micromouselab.mazes;


public record MazeCreateDTO(
    String description,
    String representation,
    MazeFormat mazeFormat
) {}