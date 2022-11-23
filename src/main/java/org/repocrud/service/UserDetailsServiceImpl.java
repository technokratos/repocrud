package org.repocrud.service;

import org.repocrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Implements the {@link UserDetailsService}.
 * <p>
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    PasswordEncoder encoder;

    /**
     * Recovers the {@link User} from the database using the e-mail address supplied
     * in the login screen. If the user is found, returns a
     * {@link org.springframework.security.core.userdetails.User}.
     *
     * @param username User's e-mail address
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public void saveUserWithEncodePassword(org.repocrud.domain.User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}