package com.micromouselab.mazes.domain;

import jakarta.validation.constraints.NotNull;

public record AuthResponseDTO (
        String accessToken,
        String refreshToken
){}
