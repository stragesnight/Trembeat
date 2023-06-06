package com.trembeat.services;

import com.trembeat.domain.models.Image;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Image storage and retrieval service
 */
@Service
public class ImageStorageService extends StorageService<Image> {
    private static String _basePath;
    private Map<String, String> _contentTypes;


    public ImageStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
        _contentTypes = new HashMap<>();
        _contentTypes.put("image/jpeg", "jpeg");
        _contentTypes.put("image/png", "png");
        _contentTypes.put("image/gif", "gif");
        _contentTypes.put("image/webp", "webp");
    }

    @Override
    public boolean save(Image id, InputStream inputStream) {
        if (!super.save(id, inputStream))
            return false;

        // TODO: replace with https://github.com/bramp/ffmpeg-cli-wrapper ???
        try {
            String inFilePath = getFullPath(id);
            String fileExtension = getFileExtension(id);
            String cmdPath = getClass().getClassLoader().getResource("resize.sh").getFile();
            String fullCmd = String.format(
                    "%s %s %s %s",
                    cmdPath,
                    inFilePath,
                    inFilePath.replace(fileExtension, "tmp." + fileExtension),
                    inFilePath.replace(fileExtension, "jpeg"));

            new File(cmdPath).setExecutable(true, true);
            Runtime.getRuntime().exec(fullCmd);

            id.setMimeType(getPreferredContentType());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected String getFullPath(Image image) {
        return String.format("%sstatic/uploads/image/%d.%s",
                _basePath,
                image.getId(),
                getFileExtension(image));
    }

    @Override
    protected String getFileExtension(Image image) {
        if (!isAcceptedContentType(image.getMimeType()))
            return "";

        return _contentTypes.get(image.getMimeType());
    }

    @Override
    public boolean isAcceptedContentType(String contentType) {
        return _contentTypes.containsKey(contentType);
    }

    @Override
    public String getPreferredContentType() {
        return "image/jpeg";
    }
}
