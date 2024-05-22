package com.ayd2.librarysystem.career.service;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.career.model.dto.CareerRequestDto;
import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareerServiceTest {

    @Mock
    CareerRepository careerRepository;

    @InjectMocks
    CareerService careerService;

    private CareerModel testCareer;
    private CareerRequestDto testCareerRequestDto;
    private CareerResponseDto testCareerResponseDto;

    @BeforeEach
    public void setUp() {
        testCareer = CareerModel.builder()
                .id(1L)
                .name("Test Career")
                .build();

        testCareerRequestDto = new CareerRequestDto(
                testCareer.getName()
        );

        testCareerResponseDto = testCareer.toRecord();
    }

    @Test
    public void itShouldGetCareerById() throws NotFoundException {
        // Arrange
        when(careerRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(testCareer));

        // Act
        var result = careerService.getCareerById(testCareer.getId());

        // Assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testCareerResponseDto);
        verify(careerRepository).findById(testCareer.getId());
    }

    @Test
    public void itShouldThrowNotFoundException_WhenCareerDoesntExist() {
        // Arrange
        when(careerRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> careerService.getCareerById(testCareer.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Career not found");
        verify(careerRepository).findById(testCareer.getId());
    }


    @Test
    public void itShouldGetAllCareers() {
        // Arrange
        when(careerRepository.findAll()).thenReturn(List.of(testCareer));

        // Act
        var result = careerService.getAllCareers();

        // Assert
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(testCareerResponseDto);
        verify(careerRepository).findAll();
    }

    @Test
    public void itShouldGetEmptyList_WhenNoCareers() {
        // Arrange
        when(careerRepository.findAll()).thenReturn(java.util.List.of());

        // Act
        var result = careerService.getAllCareers();

        // Assert
        assertThat(result).isNotNull().isEmpty();
        verify(careerRepository).findAll();
    }

    @Test
    public void itShouldCreateCareer() throws DuplicatedEntityException {
        // Arrange
        when(careerRepository.findByName(any(String.class))).thenReturn(java.util.Optional.empty());
        when(careerRepository.save(any(CareerModel.class))).thenReturn(testCareer);

        // Act
        var result = careerService.createCareer(testCareerRequestDto);

        // Assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testCareerResponseDto);
        verify(careerRepository).findByName(any(String.class));
        verify(careerRepository).save(any(CareerModel.class));
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenCareerAlreadyExists() {
        // Arrange
        when(careerRepository.findByName(any(String.class))).thenReturn(Optional.of(testCareer));

        // Act & Assert
        assertThatThrownBy(() -> careerService.createCareer(testCareerRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Career already exists");
        verify(careerRepository).findByName(any(String.class));
    }

    @Test
    public void itShouldUpdateCareer() throws NotFoundException, DuplicatedEntityException {
        // Arrange
        when(careerRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(careerRepository.findById(any(Long.class))).thenReturn(Optional.of(testCareer));
        when(careerRepository.save(any(CareerModel.class))).thenReturn(testCareer);

        // Act
        var result = careerService.updateCareer(testCareer.getId(), testCareerRequestDto);

        // Assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testCareerResponseDto);
        verify(careerRepository).findByName(any(String.class));
        verify(careerRepository).findById(testCareer.getId());
        verify(careerRepository).save(any(CareerModel.class));
    }

    @Test
    public void itShouldThrowNotFoundException_WhenCareerToUpdateDoesntExist() {
        // Arrange
        when(careerRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(careerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> careerService.updateCareer(testCareer.getId(), testCareerRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Career not found");
        verify(careerRepository).findByName(any(String.class));
        verify(careerRepository).findById(testCareer.getId());
        verify(careerRepository, never()).save(any(CareerModel.class));
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenCareerNameAlreadyExists() {
        // Arrange
        when(careerRepository.findByName(any(String.class))).thenReturn(Optional.of(testCareer));

        // Act & Assert
        assertThatThrownBy(() -> careerService.updateCareer(testCareer.getId(), testCareerRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Career with this name already exists");
        verify(careerRepository).findByName(any(String.class));
        verify(careerRepository, never()).save(any(CareerModel.class));
    }

    @Test
    public void itShouldDeleteCareer() throws NotFoundException {
        // Arrange
        when(careerRepository.findById(any(Long.class))).thenReturn(Optional.of(testCareer));

        // Act
        careerService.deleteCareer(testCareer.getId());

        // Assert
        verify(careerRepository).findById(testCareer.getId());
        verify(careerRepository).delete(testCareer);
    }

    @Test
    public void itShouldThrowNotFoundException_WhenCareerToDeleteDoesntExist() {
        // Arrange
        when(careerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> careerService.deleteCareer(testCareer.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Career not found");
        verify(careerRepository).findById(testCareer.getId());
        verify(careerRepository, never()).delete(any(CareerModel.class));
    }

}