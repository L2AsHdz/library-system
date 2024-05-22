package com.ayd2.librarysystem.parameter.service;

import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.parameter.model.ParameterModel;
import com.ayd2.librarysystem.parameter.model.dto.ParameterRequestDto;
import com.ayd2.librarysystem.parameter.model.dto.ParameterResponseDto;
import com.ayd2.librarysystem.parameter.repository.ParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterResponseDto getParameter(Long id) throws NotFoundException {
        return parameterRepository.findById(id)
                .map(ParameterModel::toRecord)
                .orElseThrow(() -> new NotFoundException("Parameter not found"));
    }

    public List<ParameterResponseDto> getParameters() {
        return parameterRepository.findAll().stream()
                .map(ParameterModel::toRecord)
                .toList();
    }

    public ParameterResponseDto updateParameter(Long id, ParameterRequestDto parameterRequestDto) throws NotFoundException {
        ParameterModel parameterModel = parameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parameter not found"));

        parameterModel.setParameterValue(parameterRequestDto.parameterValue());
        return parameterRepository.save(parameterModel).toRecord();
    }
}
