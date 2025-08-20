package com.micromouselab.mazes;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MazeDTO (
    Long id,
    String description,
    String representation,
    MazeFormat mazeFormat
) {}