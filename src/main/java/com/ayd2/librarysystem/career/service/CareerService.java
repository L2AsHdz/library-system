package com.ayd2.librarysystem.career.service;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.career.model.dto.CareerRequestDto;
import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerResponseDto getCareerById(Long id) throws NotFoundException {
        return careerRepository.findById(id)
                .map(CareerModel::toRecord)
                .orElseThrow(() -> new NotFoundException("Career not found"));
    }

    public List<CareerResponseDto> getAllCareers() {
        return careerRepository.findAll().stream()
                .map(CareerModel::toRecord)
                .toList();
    }

    public CareerResponseDto createCareer(CareerRequestDto careerModel) throws DuplicatedEntityException {
        var duplicatedByName = careerRepository.findByName(careerModel.name());
        if (duplicatedByName.isPresent())
            throw new DuplicatedEntityException("Career already exists");

        var newCareer = careerRepository.save(careerModel.toEntity());
        return newCareer.toRecord();
    }

    public CareerResponseDto updateCareer(Long id, CareerRequestDto careerModel) throws NotFoundException, DuplicatedEntityException {
        var duplicatedByName = careerRepository.findByName(careerModel.name());
        if (duplicatedByName.isPresent())
            throw new DuplicatedEntityException("Career with this name already exists");

        var careerToUpdate = careerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Career not found"));

        careerToUpdate.setName(careerModel.name());

        var updatedCareer = careerRepository.save(careerToUpdate);
        return updatedCareer.toRecord();
    }

    public void deleteCareer(Long id) throws NotFoundException {
        var careerToDelete = careerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Career not found"));

        careerRepository.delete(careerToDelete);
    }
}
