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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ContentId
    // TODO: find walid contentId length
    @Column(name = "content_id", length = 256, nullable = false, unique = true)
    protected String contentId;

    @ContentLength
    @Column(name = "content_length", nullable = false)
    protected Long contentLength;

    @Column(name = "mime_type", length = 64, nullable = false)
    protected String mimeType;

    @Column(name = "upload_date", nullable = false)
    protected Date uploadDate;


    @PrePersist
    private void prePersist() {
        uploadDate = new Date();
    }
}
