package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * Generic file entity
 */
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class FileEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "mime_type", length = 64, nullable = false)
    protected String mimeType;

    @Column(name = "upload_date", nullable = false)
    protected Date uploadDate;


    @PrePersist
    protected void prePersist() {
        uploadDate = new Date();
    }
}
