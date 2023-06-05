package com.trembeat.domain.viewmodels;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Comment view model during it's creation
 */
@Data
public class CommentCreateViewModel {
    @NotNull
    @NotEmpty
    private String text;

    private Long soundId;
}
