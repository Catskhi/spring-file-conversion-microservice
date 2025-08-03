package com.catskhi.mail.dto;

import java.util.UUID;

public record EmailDto(
        UUID id,
        String emailTo,
        String emailSubject,
        String body
) {
}
