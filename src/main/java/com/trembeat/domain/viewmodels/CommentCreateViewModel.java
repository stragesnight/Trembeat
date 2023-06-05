package com.trembeat.domain.viewmodels;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Comment view model during it's creation
 */
@Data
@RequiredArgsConstructor
public class CommentCreateViewModel {
    @NotNull
    @NotEmpty
    private String text;

    @NonNull
    private Long soundId;
}
