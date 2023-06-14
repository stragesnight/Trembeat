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
    @NotNull(message = "error.invalid_title")
    @NotEmpty(message = "error.invalid_title")
    private String title;

    @NotNull(message = "error.invalid_description")
    private String description;

    @NotNull(message = "error.invalid_file")
    private MultipartFile file;

    @NotNull(message = "error.invalid_cover")
    private MultipartFile cover;

    @NotNull(message = "error.invalid_genre")
    private Long genreId;

    @NonNull
    @NotNull(message = "error.not_an_author")
    private Long authorId;
}
