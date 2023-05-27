package com.trembeat.services;

import com.trembeat.domain.models.Sound;
import com.trembeat.domain.repository.SoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


/**
 * Sound access and management service
 */
@Service
public class SoundService {
    @Autowired
    private SoundRepository _soundRepo;


    public Iterable<Sound> findAll() {
        return _soundRepo.findAll();
    }

    public Iterable<Sound> findAllByTitle(String title) {
        return _soundRepo.findAllByTitle(title);
    }

    public boolean addSound(Sound sound, MultipartFile file) {
        try {
            sound = _soundRepo.save(sound);
        } catch (Exception ex) {
            return false;
        }

        System.out.printf("content type: %s\n", file.getContentType());
        System.out.printf("name: %s\n", file.getOriginalFilename());
        System.out.printf("sound.id: %d\n", sound.getId());

        String filepath = String.format("classpath:/static/audio/%d.mp3", sound.getId());

        OutputStream os;
        try {
            os = new BufferedOutputStream(new FileOutputStream(filepath));
            os.write(file.getBytes());
            os.flush();
            os.close();
        } catch (IOException ex) {
            return false;
        }

        return true;
    }
}
