package com.ayd2.librarysystem.user.repository;

import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserModel testUser;

    @BeforeEach
    public void setUp() {
        testUser = UserModel.builder()
                .username("test")
                .email("test@email.com")
                .userRole(Rol.LIBRARIAN)
                .fullName("Test User")
                .password("password")
                .build();
    }

    @Test
    public void itShouldFindUserByUsername() {
        // Arrange
        userRepository.save(testUser);

        // Act
        var oResult = userRepository.findByUsername(testUser.getUsername());
        var result = oResult.get();

        // Assert
        assertThat(oResult).isPresent();
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testUser);
    }

    @Test
    public void itShouldFindUserByEmail() {
        // Arrange
        userRepository.save(testUser);

        // Act
        var oResult = userRepository.findByEmail(testUser.getEmail());
        var result = oResult.get();

        // Assert
        assertThat(oResult).isPresent();
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testUser);
    }

    @Test
    public void itShouldNotFindUserByUsername() {
        // Arrange
        userRepository.save(testUser);

        // Act
        var result = userRepository.findByUsername("notfound");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void itShouldNotFindUserByEmail() {
        // Arrange
        userRepository.save(testUser);

        // Act
        var result = userRepository.findByEmail("notfound");

        // Assert
        assertThat(result).isEmpty();
    }

}