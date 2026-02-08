package com.micromouselab.mazes.domain;

import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDTO(
        @NotNull Role role
) {
}
