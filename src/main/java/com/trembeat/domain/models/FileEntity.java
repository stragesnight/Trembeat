package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.content.commons.annotations.*;

import java.util.Date;

/**
 * Generic file entity
 */
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class FileEntity {
    @Id
    @ContentId
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ContentLength
    @Column(name = "content_length", nullable = false)
    protected Long contentLength;

    @MimeType
    @Column(name = "mime_type", length = 64, nullable = false)
    protected String mimeType;

    @Column(name = "upload_date", nullable = false)
    protected Date uploadDate;


    @PrePersist
    private void prePersist() {
        uploadDate = new Date();
    }
}
