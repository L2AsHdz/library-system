package com.ayd2.librarysystem.career.repository;

import com.ayd2.librarysystem.career.model.CareerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class CareerRepositoryTest {

    @Autowired
    private CareerRepository careerRepository;

    private CareerModel testCareer;

    @BeforeEach
    public void setUp() {
        testCareer = CareerModel.builder()
                .name("Test Career")
                .build();
    }

    @Test
    public void itShouldFindCareerByName() {
        // Arrange
        careerRepository.save(testCareer);

        // Act
        var oResult = careerRepository.findByName(testCareer.getName());
        var result = oResult.get();

        // Assert
        assertThat(oResult).isPresent();
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testCareer);
    }

    @Test
    public void itShouldNotFindCareerByName() {
        // Arrange
        careerRepository.save(testCareer);

        // Act
        var oResult = careerRepository.findByName("Another Career");

        // Assert
        assertThat(oResult).isEmpty();
    }

}