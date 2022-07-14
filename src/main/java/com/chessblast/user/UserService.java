package com.chessblast.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String signUpUser(User user) throws IllegalAccessException {
        boolean userExist = userRepository.findByUsername(user.getUsername()).isPresent();

        if (userExist) {
            throw new IllegalAccessException("Username already taken!");
        } else {
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            userRepository.save(user);

            // TODO: Send confirmation token

            return "It Works";
        }
    }
}
