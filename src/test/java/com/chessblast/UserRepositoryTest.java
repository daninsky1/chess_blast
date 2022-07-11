package com.chessblast;

import static org.assertj.core.api.Assertions.assertThat;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {
        userRepository.deleteAll();
        List<User> users = new ArrayList<>();
        users.add(new User("daninsky", "dan@dan.com", "1234"));
        users.add(new User("marina", "ma@dan.com", "1234"));
        users.add(new User("jose", "jose@dan.com", "1234"));
        users.add(new User("zilda", "zilda@dan.com", "1234"));

        for (User user : users) {
            User savedUser = userRepository.save(user);
            User userExist = testEntityManager.find(User.class, savedUser.getId());

            assertThat(userExist.getEmail()).isEqualTo(user.getEmail());
        }
    }
}
