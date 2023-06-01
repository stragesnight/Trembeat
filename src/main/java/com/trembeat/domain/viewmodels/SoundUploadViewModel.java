package com.trembeat.domain.viewmodels;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Sound view model
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SoundUploadViewModel {
    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    private String description;

    @NotNull
    private MultipartFile file;

    @NotNull
    private Long genreId;

    @NotNull
    @NonNull
    private Long authorId;
}
