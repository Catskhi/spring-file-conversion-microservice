package com.catskhi.mail.domain;

import com.catskhi.mail.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emails")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailModel {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String emailTo;

    @Column(nullable = false)
    private String emailSubject;

    @Column(nullable = false, length = 5000, columnDefinition = "TEXT")
    private String body;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

}
