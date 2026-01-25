package com.micromouselab.mazes.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO (
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String plaintextPassword
){}
