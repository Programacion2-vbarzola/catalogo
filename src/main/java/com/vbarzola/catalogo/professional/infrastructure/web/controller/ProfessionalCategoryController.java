package com.vbarzola.catalogo.professional.infrastructure.web.controller;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalCategoryException;
import com.vbarzola.catalogo.professional.application.service.ProfessionalCategoryService;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryRequest;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryResponse;
import com.vbarzola.catalogo.professional.infrastructure.web.mapper.ProfessionalCategoryDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
@Tag(name = "Professional Categories", description = "Gestión de categorías de profesionales del catálogo")
public class ProfessionalCategoryController {

    private final ProfessionalCategoryService professionalCategoryService;
    private final ProfessionalCategoryDtoMapper dtoMapper;

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Guarda una nueva categoría de profesionales")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    public ResponseEntity<ProfessionalCategoryResponse> createProfessionalCategory(@Valid @RequestBody ProfessionalCategoryRequest request)
        throws URISyntaxException {
        ProfessionalCategory domain = dtoMapper.toDomain(request);
        ProfessionalCategory created = professionalCategoryService.createProfessionalCategory(domain);
        return ResponseEntity.created(new URI("/api/professional-categories/" + created.getId())).body(dtoMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría específica")
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
    @Operation(summary = "Listar todas las categorías", description = "Retorna la lista completa de categorías de profesionales")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    public ResponseEntity<List<ProfessionalCategoryResponse>> getAllProfessionalCategories() {
        List<ProfessionalCategoryResponse> responses = professionalCategoryService
            .getAllProfessionalCategories()
            .stream()
            .map(dtoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza completamente una categoría existente")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    public ResponseEntity<ProfessionalCategoryResponse> updateProfessionalCategory(
        @PathVariable Long id,
        @Valid @RequestBody ProfessionalCategoryRequest request
    ) {
        ProfessionalCategory domain = dtoMapper.toDomain(request);
        return professionalCategoryService
            .updateProfessionalCategory(id, domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(summary = "Actualizar parcialmente categoría", description = "Actualiza solo los campos proporcionados de una categoría")
    @ApiResponse(responseCode = "200", description = "Categoría parcialmente actualizada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    public ResponseEntity<ProfessionalCategoryResponse> partialUpdateProfessionalCategory(
        @PathVariable Long id,
        @RequestBody ProfessionalCategoryRequest request
    ) {
        ProfessionalCategory domain = dtoMapper.toDomain(request);
        domain.setId(id);
        return professionalCategoryService
            .partialUpdateProfessionalCategory(domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    @ApiResponse(responseCode = "204", description = "Categoría eliminada")
    public ResponseEntity<Void> deleteProfessionalCategory(@PathVariable Long id) {
        professionalCategoryService.deleteProfessionalCategory(id);
        return ResponseEntity.noContent().build();
    }
}
