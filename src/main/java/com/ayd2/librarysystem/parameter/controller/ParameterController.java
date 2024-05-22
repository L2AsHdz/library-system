package com.ayd2.librarysystem.parameter.controller;

import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.parameter.model.dto.ParameterRequestDto;
import com.ayd2.librarysystem.parameter.model.dto.ParameterResponseDto;
import com.ayd2.librarysystem.parameter.service.ParameterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/parameters")
@RequiredArgsConstructor
public class ParameterController {

    private final ParameterService parameterService;

    @GetMapping
    public ResponseEntity<List<ParameterResponseDto>> getParameters() {
        return ResponseEntity.ok(parameterService.getParameters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParameterResponseDto> getParameter(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(parameterService.getParameter(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParameterResponseDto> updateParameter(@PathVariable Long id, @RequestBody @Valid ParameterRequestDto parameterRequestDto) throws NotFoundException {
        return ResponseEntity.ok(parameterService.updateParameter(id, parameterRequestDto));
    }
}
