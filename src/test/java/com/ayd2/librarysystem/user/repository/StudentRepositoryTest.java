package com.ayd2.librarysystem.user.repository;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.user.model.StudentModel;
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
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CareerRepository careerRepository;

    private UserModel testUser;
    private CareerModel testCareer;
    private StudentModel testStudent;

    @BeforeEach
    public void setUp() {
        testUser = UserModel.builder()
                .username("test")
                .email("test@email.com")
                .userRole(Rol.LIBRARIAN)
                .fullName("Test User")
                .password("password")
                .build();

        testCareer = CareerModel.builder()
                .name("Test Career")
                .build();

        testStudent = new StudentModel();
        testStudent.setAcademicNumber(1234567890L);
        testStudent.setFullName("Test Student");

        testStudent.setFullName(testUser.getFullName());
        testStudent.setEmail(testUser.getEmail());
        testStudent.setUsername(testUser.getUsername());
        testStudent.setPassword(testUser.getPassword());
        testStudent.setUserRole(testUser.getUserRole());
    }

    @Test
    public void itShouldFindStudentByAcademicNumber() {
        // Arrange
        var career = careerRepository.save(testCareer);
        testStudent.setCareerModel(career);
        studentRepository.save(testStudent);

        // Act
        var oResult = studentRepository.findByAcademicNumber(testStudent.getAcademicNumber());
        var result = oResult.get();

        // Assert
        assertThat(oResult).isPresent();
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testStudent);
    }

    @Test
    public void itShouldNotFindStudentByAcademicNumber() {
        // Arrange
        var career = careerRepository.save(testCareer);
        testStudent.setCareerModel(career);
        studentRepository.save(testStudent);

        // Act
        var oResult = studentRepository.findByAcademicNumber(0L);

        // Assert
        assertThat(oResult).isEmpty();
    }

}