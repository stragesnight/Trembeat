package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.Sound;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Sound view model during display
 */
@Data
public class SoundViewModel {
    private Long id;

    private String title;

    private String description;

    private Float length;

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
        length = sound.getLength();
        genreName = sound.getGenre().getName();
        mimeType = sound.getMimeType();
        author = new UserViewModel(sound.getAuthor());
        uploadDate = sound.getUploadDate();
        lastBumpDate = sound.getLastBumpDate();
        cover = new FileEntityViewModel(sound.getCover());
    }

    public String getFormattedLength() {
        int h = length.intValue() / 3600;
        int m = (length.intValue() / 60) % 60;
        int s = length.intValue() % 60;

        return h < 1
                ? String.format("%d:%s%d", m, s < 10 ? "0" : "", s)
                : String.format("%d:%s%d:%s%d", h, m < 10 ? "0" : "", m, s < 10 ? "0" : "", s);
    }
}
