package com.vbarzola.catalogo.professional.infrastructure.web.controller;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalCategoryException;
import com.vbarzola.catalogo.professional.application.service.ProfessionalCategoryService;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryResponse;
import com.vbarzola.catalogo.professional.infrastructure.web.mapper.ProfessionalCategoryDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/professional-categories")
@RequiredArgsConstructor
@Tag(name = "Professional Categories", description = "Consulta de categorías de profesionales del catálogo")
public class ProfessionalCategoryController {

    private final ProfessionalCategoryService professionalCategoryService;
    private final ProfessionalCategoryDtoMapper dtoMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    public ResponseEntity<ProfessionalCategoryResponse> getProfessionalCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dtoMapper.toResponse(professionalCategoryService.getProfessionalCategoryById(id)));
        } catch (ProfessionalCategoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    public ResponseEntity<List<ProfessionalCategoryResponse>> getAllProfessionalCategories() {
        List<ProfessionalCategoryResponse> responses = professionalCategoryService
            .getAllProfessionalCategories()
            .stream()
            .map(dtoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
