package com.trembeat.services;

import com.trembeat.domain.models.User;
import com.trembeat.domain.repository.UserRepository;
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
     * Register new user
     * @param user User data to be registered
     * @return true, if user was registered successfully, otherwise - false
     */
    public boolean registerUser(User user) {
        try {
            user.setPassword(_passwordEncoder.encode(user.getPassword()));
            user.setPasswordConfirmation(user.getPassword());
            _userRepo.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
