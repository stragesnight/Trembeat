package com.trembeat.services;

import com.trembeat.domain.models.*;
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
    private ImageRepository _imageRepo;
    @Autowired
    private ImageStorageService _storageService;
    private RoleRepository _roleRepo;
    private BCryptPasswordEncoder _passwordEncoder;

    private Role _roleAdmin;
    private Role _roleUser;


    public UserService(RoleRepository roleRepository) {
        _passwordEncoder = new BCryptPasswordEncoder();

        _roleRepo = roleRepository;
        _roleAdmin = _roleRepo.findByName("ROLE_ADMIN").get();
        _roleUser = _roleRepo.findByName("ROLE_USER").get();
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
    public User registerUser(UserRegisterViewModel viewModel) {
        try {
            User user = new User(
                    viewModel.getUsername(),
                    "",
                    _passwordEncoder.encode(viewModel.getPassword()),
                    viewModel.getEmail());

            user.addAuthority(_roleUser);
            if (_userRepo.findAll().isEmpty())
                user.addAuthority(_roleAdmin);

            user.setProfilePicture(_imageRepo.findById(1L).get());

            _userRepo.save(user);

            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Update existing user
     * @param userId Existing user id to be updated
     * @param viewModel Updated user date
     * @return true, if user was updated successfully, otherwise - false
     */
    public User updateUser(Long userId, UserEditViewModel viewModel) {
        try {
            User user = _userRepo.findById(userId).get();
            user.setUsername(viewModel.getUsername());
            user.setBio(viewModel.getBio());

            if (viewModel.getPassword() != null && !viewModel.getPassword().isEmpty()) {
                user.setPassword(_passwordEncoder.encode(viewModel.getPassword()));
                user.setEmail(viewModel.getEmail());
            }

            if (viewModel.getProfilePicture() != null) {
                Image picture = new Image();
                picture.setMimeType(viewModel.getProfilePicture().getContentType());
                if (!_storageService.isAcceptedContentType(picture.getMimeType()))
                    return null;

                picture = _imageRepo.save(picture);
                _storageService.save(picture, viewModel.getProfilePicture().getInputStream());
                picture = _imageRepo.save(picture);
                user.setProfilePicture(picture);
            }

            user = _userRepo.save(user);
            return user;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
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
            _storageService.delete(user.getProfilePicture());
            _userRepo.delete(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
