package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.FileEntity;
import lombok.Data;

import java.util.Date;

/**
 * Generic FileEntity view model
 */
@Data
public class FileEntityViewModel {
    private Long id;

    private String mimeType;

    private Date uploadDate;


    public FileEntityViewModel(FileEntity fileEntity) {
        if (fileEntity == null)
            return;

        id = fileEntity.getId();
        mimeType = fileEntity.getMimeType();
        uploadDate = fileEntity.getUploadDate();
    }
}
