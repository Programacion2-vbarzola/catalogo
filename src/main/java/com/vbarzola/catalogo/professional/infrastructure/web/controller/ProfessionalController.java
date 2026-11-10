package com.vbarzola.catalogo.professional.infrastructure.web.controller;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalException;
import com.vbarzola.catalogo.professional.application.service.ProfessionalService;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalResponse;
import com.vbarzola.catalogo.professional.infrastructure.web.mapper.ProfessionalDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/professionals")
@RequiredArgsConstructor
@Tag(name = "Professionals", description = "Consulta de profesionales del catálogo")
public class ProfessionalController {

    private final ProfessionalService professionalService;
    private final ProfessionalDtoMapper dtoMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener profesional por ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    public ResponseEntity<ProfessionalResponse> getProfessional(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dtoMapper.toResponse(professionalService.getProfessionalById(id)));
        } catch (ProfessionalException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Listar profesionales con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de profesionales")
    public ResponseEntity<List<ProfessionalResponse>> getAllProfessionals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        Page<ProfessionalResponse> page = eagerload
            ? professionalService.getAllProfessionalsWithEagerRelationships(pageable).map(dtoMapper::toResponse)
            : professionalService.getAllProfessionals(pageable).map(dtoMapper::toResponse);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar profesionales por nombre, categoría y/o estado")
    @ApiResponse(responseCode = "200", description = "Resultados de búsqueda")
    public ResponseEntity<List<ProfessionalResponse>> searchProfessionals(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Boolean enabled,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<ProfessionalResponse> page = professionalService.search(name, categoryId, enabled, pageable).map(dtoMapper::toResponse);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
