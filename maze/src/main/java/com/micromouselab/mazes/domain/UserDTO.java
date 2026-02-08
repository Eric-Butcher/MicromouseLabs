package com.micromouselab.mazes.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO (
        @NotNull Long id,
        @NotNull @NotBlank String username,
        @NotNull Role role

){}
