package com.catskhi.processing.dto;

import java.util.UUID;

public record VideoDto(
        UUID userId,
        String userEmail,
        String videoId
) {
}
