package com.trembeat.services;

import com.trembeat.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


/**
 * User access service, implements UserDetailsService
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository _userRepo;

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
}
