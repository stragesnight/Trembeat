package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.Sound;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Sound view model during editing
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SoundEditViewModel {

    private Long id;

    @NotNull(message = "error.invalid_title")
    @NotEmpty(message = "error.invalid_title")
    private String title;

    @NotNull(message = "error.invalid_description")
    private String description;

    private MultipartFile cover;

    @NotNull(message = "error.invalid_genre")
    private Long genreId;

    @NotNull(message = "error.invalid_author")
    @NonNull
    private Long authorId;


    public SoundEditViewModel(Sound sound) {
        id = sound.getId();
        title = sound.getTitle();
        description = sound.getDescription();
        genreId = sound.getGenre().getId();
        authorId = sound.getAuthor().getId();
    }
}
