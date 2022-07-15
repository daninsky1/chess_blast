package com.example.chessblast.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User: \"%s\" not found.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username))
        );
    }

    public void signUpUser(User user) throws UserAlreadyExistException, EmailAlreadyExistException {
        boolean userExist = userRepository.findByUsername(user.getUsername()).isPresent();
        boolean emailExist = userRepository.findByUsername(user.getEmail()).isPresent();

        if (userExist) {
            throw new UserAlreadyExistException("Username already taken!");
        }
        else if (emailExist) {
            throw new EmailAlreadyExistException("Email already taken!");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}
