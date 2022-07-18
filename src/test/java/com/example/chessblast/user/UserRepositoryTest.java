package com.example.chessblast.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    List<User> users = new ArrayList<>();

    public UserRepositoryTest() {
        users = Arrays.asList(
            new User("daniel", "dan@sky.com", "passworddaniel"),
            new User("marina", "mari@sky.com", "passwordmarina"),
            new User("jose", "jose@sky.com", "passwordjose"),
            new User("zilda", "zilda@sky.com", "passwordzilda")
        );
    }

    @Test
    public void saveUser() {
        for (User user : users) {
            User savedUser = userRepository.save(user);

            assertThat(savedUser.getId()).isEqualTo(user.getId());
            assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
            assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        }
    }

    @Test
    public void findUserByUsername() {
        String username = "zilda";

        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isNotNull();
        System.out.println(user);
    }
}
