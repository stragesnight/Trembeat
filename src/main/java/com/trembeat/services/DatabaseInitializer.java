package com.trembeat.services;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    @Autowired
    private RoleRepository _roleRepo;
    @Autowired
    private GenreRepository _genreRepo;
    @Autowired
    private ImageRepository _imageRepo;
    @Autowired
    private UserRepository _userRepo;


    @Override
    public void run(ApplicationArguments args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role roleAdmin = _roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> _roleRepo.save(new Role("ROLE_ADMIN")));
        Role roleUser = _roleRepo.findByName("ROLE_USER").orElseGet(() -> _roleRepo.save(new Role("ROLE_USER")));

        if (_genreRepo.count() < 1) {
            _genreRepo.save(new Genre("alternative"));
            _genreRepo.save(new Genre("ambient"));
            _genreRepo.save(new Genre("blues"));
            _genreRepo.save(new Genre("classical"));
            _genreRepo.save(new Genre("edm"));
            _genreRepo.save(new Genre("folk"));
            _genreRepo.save(new Genre("jazz"));
            _genreRepo.save(new Genre("metal"));
            _genreRepo.save(new Genre("new_age"));
            _genreRepo.save(new Genre("pop"));
            _genreRepo.save(new Genre("rnb"));
            _genreRepo.save(new Genre("rock"));
            _genreRepo.save(new Genre("other"));
        }

        Image defaultImage = _imageRepo.findById(1L).orElseGet(() -> {
            Image img = new Image();
            img.setMimeType("image/jpeg");
            img.setUploadDate(new Date());
            return _imageRepo.save(img);
        });

        if (_userRepo.count() > 0)
            return;

        User admin = new User(
                "admin",
                "admin",
                encoder.encode("admin"),
                "admin@admin.com");

        admin.setProfilePicture(defaultImage);
        admin.addAuthority(roleAdmin);
        admin.addAuthority(roleUser);

        _userRepo.save(admin);
    }
}
