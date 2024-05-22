package com.ayd2.librarysystem.parameter.service;

import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.parameter.model.ParameterModel;
import com.ayd2.librarysystem.parameter.model.dto.ParameterRequestDto;
import com.ayd2.librarysystem.parameter.model.dto.ParameterResponseDto;
import com.ayd2.librarysystem.parameter.repository.ParameterRepository;
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
class ParameterServiceTest {

    @Mock
    ParameterRepository parameterRepository;

    @InjectMocks
    ParameterService parameterService;

    private ParameterModel testParameter;
    private ParameterRequestDto testParameterRequestDto;
    private ParameterResponseDto testParameterResponseDto;

    @BeforeEach
    public void setUp() {
        testParameter = ParameterModel.builder()
                .id(1L)
                .parameterName("Test Parameter")
                .parameterValue(1.0)
                .build();

        testParameterRequestDto = new ParameterRequestDto(
                testParameter.getId(),
                testParameter.getParameterValue()
        );

        testParameterResponseDto = testParameter.toRecord();
    }

    @Test
    public void itShouldGetParameterById() throws NotFoundException {
        // Arrange
        when(parameterRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(testParameter));

        // Act
        var result = parameterService.getParameter(testParameter.getId());

        // Assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testParameterResponseDto);
        verify(parameterRepository).findById(testParameter.getId());
    }

    @Test
    public void itShouldThrowNotFoundExceptionWhenParameterNotFound() {
        // Arrange
        when(parameterRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> parameterService.getParameter(testParameter.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parameter not found");
        verify(parameterRepository).findById(testParameter.getId());
    }

    @Test
    public void itShouldGetAllParameters() {
        // Arrange
        when(parameterRepository.findAll()).thenReturn(List.of(testParameter));

        // Act
        var result = parameterService.getParameters();

        // Assert
        assertThat(result).isNotNull().hasSize(1).contains(testParameterResponseDto);
        verify(parameterRepository).findAll();
    }

    @Test
    public void itShouldGetEmptyListWhenNoParameters() {
        // Arrange
        when(parameterRepository.findAll()).thenReturn(List.of());

        // Act
        var result = parameterService.getParameters();

        // Assert
        assertThat(result).isNotNull().isEmpty();
        verify(parameterRepository).findAll();
    }

    @Test
    public void itShouldUpdateParameter() throws NotFoundException {
        // Arrange
        when(parameterRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(testParameter));
        when(parameterRepository.save(any(ParameterModel.class))).thenReturn(testParameter);

        // Act
        var result = parameterService.updateParameter(testParameter.getId(), testParameterRequestDto);

        // Assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testParameterResponseDto);
        verify(parameterRepository).findById(testParameter.getId());
        verify(parameterRepository).save(testParameter);
    }

    @Test
    public void itShouldThrowNotFoundExceptionWhenParameterToUpdateNotFound() {
        // Arrange
        when(parameterRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> parameterService.updateParameter(testParameter.getId(), testParameterRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parameter not found");
        verify(parameterRepository).findById(testParameter.getId());
        verify(parameterRepository, never()).save(testParameter);
    }

}