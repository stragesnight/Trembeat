package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.Sound;
import lombok.Data;

import java.util.Date;

/**
 * Sound view model during display
 */
@Data
public class SoundViewModel {
    private Long id;

    private String title;

    private String description;

    private String genreName;

    private String mimeType;

    private UserViewModel author;

    private Date uploadDate;

    private Date lastBumpDate;

    private FileEntityViewModel cover;


    public SoundViewModel(Sound sound) {
        id = sound.getId();
        title = sound.getTitle();
        description = sound.getDescription();
        genreName = sound.getGenre().getName();
        mimeType = sound.getMimeType();
        author = new UserViewModel(sound.getAuthor());
        uploadDate = sound.getUploadDate();
        lastBumpDate = sound.getLastBumpDate();
        cover = new FileEntityViewModel(sound.getCover());
    }
}
