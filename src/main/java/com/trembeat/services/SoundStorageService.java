package com.trembeat.services;


import com.trembeat.domain.models.Sound;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Sound data storage and retrieval service
 */
@Service
public class SoundStorageService extends StorageService<Sound> {
    private static String _basePath;
    private static Map<String, String> _contentTypes;


    public SoundStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
        _contentTypes = new HashMap<>();
        _contentTypes.put("audio/mpeg", "mp3");
        _contentTypes.put("audio/wav", "wav");
        _contentTypes.put("audio/x-wav", "wav");
        _contentTypes.put("audio/opus", "opus");
        _contentTypes.put("audio/ogg", "ogg");
    }

    @Override
    public boolean save(Sound sound, InputStream inputStream) {
        if (!super.save(sound, inputStream))
            return false;

        try {
            String cmdPath = getClass().getClassLoader().getResource("getlength.sh").getFile();
            String fullCmd = String.format("%s %s", cmdPath, getFullPath(sound));
            new File(cmdPath).setExecutable(true, true);

            Process process = Runtime.getRuntime().exec(fullCmd);
            process.waitFor();

            BufferedReader reader = process.inputReader();
            sound.setLength(Float.parseFloat(reader.readLine()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected String getFullPath(Sound sound) {
        return String.format("%sstatic/uploads/sound/%d.%s",
                _basePath,
                sound.getId(),
                getFileExtension(sound));
    }

    @Override
    protected String getFileExtension(Sound sound) {
        if (!isAcceptedContentType(sound.getMimeType()))
            return "";

        return _contentTypes.get(sound.getMimeType());
    }

    @Override
    public boolean isAcceptedContentType(String contentType) {
        return _contentTypes.containsKey(contentType);
    }

    @Override
    public String getPreferredContentType() {
        return "audio/mpeg";
    }
}
