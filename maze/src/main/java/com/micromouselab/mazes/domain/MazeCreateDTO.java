package com.micromouselab.mazes.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MazeCreateDTO(
    @NotNull @NotBlank String description,
    @NotNull @NotBlank String representation,
    @NotNull MazeFormat mazeFormat
) {}