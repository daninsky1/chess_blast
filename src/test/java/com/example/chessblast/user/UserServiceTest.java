package com.example.chessblast.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    private List<User> users;

    public UserServiceTest() {
        users = Arrays.asList(
            new User("daniel", "dan@sky.com", "password"),
            new User("marina", "mari@sky.com", "password"),
            new User("ana", "ana@sky.com", "password"),
            new User("bruno", "bruno@sky.com", "password")
        );
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void signUpUser() throws UserAlreadyExistException, EmailAlreadyExistException {
        for (User user : users) {
            userService.signUpUser(user);
        }
    }

    @Test
    void signUpUserUsernameExist() {

    }
}