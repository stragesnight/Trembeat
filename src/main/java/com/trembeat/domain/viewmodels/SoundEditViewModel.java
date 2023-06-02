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

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    private String description;

    private MultipartFile cover;

    @NotNull
    private Long genreId;

    @NotNull
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
