package com.trembeat.services;

import com.trembeat.domain.models.User;
import com.trembeat.domain.repository.UserRepository;
import com.trembeat.domain.viewmodels.UserEditViewModel;
import com.trembeat.domain.viewmodels.UserRegisterViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


/**
 * User access service, implements UserDetailsService
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository _userRepo;
    private BCryptPasswordEncoder _passwordEncoder;


    public UserService() {
        _passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Find user by username
     * @param username Username to search for
     * @return Found UserDetails interface
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return _userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found"));
    }

    /**
     * Find user by id
     * @param id Id to search for
     * @return Found User entity
     */
    public User findById(Long id) {
        return _userRepo.findById(id).orElse(null);
    }

    /**
     * Register new user
     * @param viewModel User data to be registered
     * @return true, if user was registered successfully, otherwise - false
     */
    public boolean registerUser(UserRegisterViewModel viewModel) {
        try {
            User user = new User(
                    viewModel.getUsername(),
                    "",
                    _passwordEncoder.encode(viewModel.getPassword()),
                    viewModel.getEmail());

            _userRepo.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Update existing user
     * @param userId Existing user id to be updated
     * @param viewModel Updated user date
     * @return true, if user was updated successfully, otherwise - false
     */
    public boolean updateUser(Long userId, UserEditViewModel viewModel) {
        try {
            User user = _userRepo.findById(userId).get();
            user.setUsername(viewModel.getUsername());
            user.setBio(viewModel.getBio());

            if (viewModel.getPassword() != null && !viewModel.getPassword().isEmpty()) {
                user.setPassword(_passwordEncoder.encode(viewModel.getPassword()));
                user.setEmail(viewModel.getEmail());
            }

            if (viewModel.getProfilePicture() != null)
                addProfilePicture(userId, viewModel.getProfilePicture());

            _userRepo.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Add profile picture for a given user
     * @param userId User id to assign profile picture to
     * @param file File containing image data
     * @return true, if file was saved successfully, otherwise - false
     */
    private boolean addProfilePicture(Long userId, MultipartFile file) {
        String filepath = String.format("%s/static/uploads/profile-pictures/%d.jpg",
                getClass().getClassLoader().getResource(".").getFile(),
                userId);

        try {
            File outputFile = new File(filepath);
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile));
            os.write(file.getBytes());
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
