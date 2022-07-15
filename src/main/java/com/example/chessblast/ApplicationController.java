package com.example.chessblast;

import com.example.chessblast.game.Game;
import com.example.chessblast.user.EmailAlreadyExistException;
import com.example.chessblast.user.User;
import com.example.chessblast.user.UserAlreadyExistException;
import com.example.chessblast.user.UserRepository;
import com.example.chessblast.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@AllArgsConstructor
public class ApplicationController {
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String homePage() {
        return "index";
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("game", new Game());
        return "signup";
    }

    @PostMapping("/process_registration")
    public String processRegistration(User user, Model model) {
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        try {
            userService.signUpUser(user);
        }
        catch (UserAlreadyExistException | EmailAlreadyExistException e) {
            model.addAttribute("errormsg", e.getMessage());
            return "signup_fail";
        }
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String passwordHash = passwordEncoder.encode(user.getPassword());
//        user.setPassword(passwordHash);
//
//        userRepository.save(user);
        return "signup_success";
    }

    @GetMapping(path = "user/{userId}")
    @ResponseBody
    public User getUser(@PathVariable("userId") Long userId) throws UsernameNotFoundException {
        return userRepository.findAll().stream().filter(user -> userId.equals(user.getId()))
            .findFirst().orElseThrow(() ->
                new UsernameNotFoundException("User with id: " + userId + " not found!"));
    }

    @GetMapping("/login")
    public String loginPage(User user) {
        return "login";
    }

    @GetMapping("/chess_lobby")
    public String chessLobbyPage(Model model) {
        List<User> users = (List<User>)userRepository.findAll();
        model.addAttribute("users", users);

        return "chess_lobby";
    }
}
