package com.catskhi.mail.dto;

import java.util.UUID;

public record EmailDto(
        UUID id,
        UUID userId,
        String emailTo,
        String emailSubject,
        String body
) {
}
