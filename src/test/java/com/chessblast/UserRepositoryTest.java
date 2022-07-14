package com.chessblast;

import static org.assertj.core.api.Assertions.assertThat;

import com.chessblast.user.User;
import com.chessblast.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    List<User> users = new ArrayList<>();

    public UserRepositoryTest() {
        users.add(new User(1, "daninsky", "dan@dan.com", "1234"));
        users.add(new User(2, "marina", "ma@dan.com", "1234"));
        users.add(new User(3, "jose", "jose@dan.com", "1234"));
        users.add(new User(4, "zilda", "zilda@dan.com", "1234"));
    }

    @Test
    public void testSaveUser() {

        for (User user : users) {
            User savedUser = userRepository.save(user);
            User userExist = testEntityManager.find(User.class, savedUser.getId());

            assertThat(userExist.getEmail()).isEqualTo(user.getEmail());
        }
    }

    @Test
    public void testFindUserByUsername() {
        String username = "daninsky";

        for (User user : users) {
            User savedUser = userRepository.save(user);
        }

        User user = userRepository.findByUsername(username);

        assertThat(user).isNotNull();
        System.out.println(user);
    }
}
