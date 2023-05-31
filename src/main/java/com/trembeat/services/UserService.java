package com.trembeat.services;

import com.trembeat.domain.models.ProfilePicture;
import com.trembeat.domain.models.User;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * User access service, implements UserDetailsService
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository _userRepo;
    @Autowired
    private ProfilePictureRepository _profilePictureRepo;
    @Autowired
    private ProfilePictureStorageService _profilePictureStorageService;
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

            if (viewModel.getProfilePicture() != null) {
                ProfilePicture picture = new ProfilePicture();
                picture.setMimeType(viewModel.getProfilePicture().getContentType());
                picture.setContentLength(viewModel.getProfilePicture().getSize());
                picture = _profilePictureRepo.save(picture);

                user.setProfilePicture(picture);
                _profilePictureStorageService.save(picture, viewModel.getProfilePicture().getInputStream());
            }

            _userRepo.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Delete user from database
     * @param userId Id of user to be deleted
     * @return true, if user was deleted successfully, otherwise - false
     */
    public boolean deleteUser(Long userId) {
        try {
            User user = _userRepo.findById(userId).get();
            _profilePictureStorageService.delete(user.getProfilePicture());
            _userRepo.delete(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
