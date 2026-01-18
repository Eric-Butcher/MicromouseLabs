package com.micromouselab.mazes;


import org.hibernate.annotations.NotFound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MazeDTO (
    @NotNull Long id,
    @NotNull @NotBlank String description,
    @NotNull @NotBlank String representation,
    @NotNull MazeFormat mazeFormat
) {}