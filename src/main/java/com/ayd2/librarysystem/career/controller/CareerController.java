package com.ayd2.librarysystem.career.controller;

import com.ayd2.librarysystem.career.model.dto.CareerRequestDto;
import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import com.ayd2.librarysystem.career.service.CareerService;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerService careerService;

    @GetMapping
    public ResponseEntity<List<CareerResponseDto>> getAllCareers() {
        return ResponseEntity.ok(careerService.getAllCareers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareerResponseDto> getCareerById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(careerService.getCareerById(id));
    }

    @PostMapping
    public ResponseEntity<CareerResponseDto> createCareer(@RequestBody @Valid CareerRequestDto careerRequestDto) throws DuplicatedEntityException {
        return ResponseEntity.created(null).body(careerService.createCareer(careerRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareerResponseDto> updateCareer(@PathVariable Long id, @RequestBody @Valid CareerRequestDto careerRequestDto) throws NotFoundException, DuplicatedEntityException {
        return ResponseEntity.ok(careerService.updateCareer(id, careerRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareer(@PathVariable Long id) throws NotFoundException {
        careerService.deleteCareer(id);
        return ResponseEntity.accepted().build();
    }
}

