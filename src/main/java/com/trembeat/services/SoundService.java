package com.trembeat.services;

import com.trembeat.domain.models.Sound;
import com.trembeat.domain.models.User;
import com.trembeat.domain.repository.GenreRepository;
import com.trembeat.domain.repository.SoundRepository;
import com.trembeat.domain.viewmodels.SoundViewModel;
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
    @Autowired
    private GenreRepository _genreRepo;


    public Iterable<Sound> findAll() {
        return _soundRepo.findAll();
    }

    public Iterable<Sound> findAllByTitle(String title) {
        return _soundRepo.findAllByTitle(title);
    }

    public boolean addSound(SoundViewModel soundViewModel, User user) {
        Sound sound = new Sound(
                soundViewModel.getTitle(),
                soundViewModel.getDescription(),
                // TODO: genre validation
                _genreRepo.findById(soundViewModel.getGenreId()).get(),
                user);

        try {
            sound = _soundRepo.save(sound);
        } catch (Exception ex) {
            return false;
        }

        String filepath = String.format("%s/static/uploads/audio/%d.mp3",
                getClass().getClassLoader().getResource(".").getFile(),
                sound.getId());

        try {
            File outputFile = new File(filepath);
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile));
            os.write(soundViewModel.getFile().getBytes());
            os.flush();
            os.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
