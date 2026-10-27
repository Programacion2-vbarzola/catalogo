package com.vbarzola.catalogo.professional.infrastructure.web.controller;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalException;
import com.vbarzola.catalogo.professional.application.service.ProfessionalService;
import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalRequest;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalResponse;
import com.vbarzola.catalogo.professional.infrastructure.web.mapper.ProfessionalDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
@Tag(name = "Professionals", description = "Gestión de profesionales del catálogo")
public class ProfessionalController {

    private final ProfessionalService professionalService;
    private final ProfessionalDtoMapper dtoMapper;

    @PostMapping
    @Operation(summary = "Crear profesional", description = "Guarda un nuevo profesional")
    @ApiResponse(responseCode = "201", description = "Profesional creado exitosamente")
    public ResponseEntity<ProfessionalResponse> createProfessional(@Valid @RequestBody ProfessionalRequest request)
        throws URISyntaxException {
        Professional domain = dtoMapper.toDomain(request);
        Professional created = professionalService.createProfessional(domain);
        return ResponseEntity.created(new URI("/api/professionals/" + created.getId())).body(dtoMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener profesional por ID", description = "Retorna un profesional específico con sus relaciones")
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
    @Operation(summary = "Listar profesionales", description = "Retorna la lista de profesionales con paginación y carga de relaciones")
    @ApiResponse(responseCode = "200", description = "Lista de profesionales")
    public ResponseEntity<List<ProfessionalResponse>> getAllProfessionals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        Page<Professional> page = eagerload
            ? professionalService.getAllProfessionalsWithEagerRelationships(pageable)
            : professionalService.getAllProfessionals(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.map(dtoMapper::toResponse).getContent());
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar profesionales", description = "Filtra profesionales por nombre, categoría y/o estado habilitado")
    @ApiResponse(responseCode = "200", description = "Resultados de búsqueda")
    public ResponseEntity<List<ProfessionalResponse>> searchProfessionals(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Boolean enabled,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<Professional> page = professionalService.search(name, categoryId, enabled, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.map(dtoMapper::toResponse).getContent());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar profesional", description = "Actualiza completamente un profesional existente")
    @ApiResponse(responseCode = "200", description = "Profesional actualizado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    public ResponseEntity<ProfessionalResponse> updateProfessional(@PathVariable Long id, @Valid @RequestBody ProfessionalRequest request) {
        Professional domain = dtoMapper.toDomain(request);
        return professionalService
            .updateProfessional(id, domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(summary = "Actualizar parcialmente profesional", description = "Actualiza solo los campos proporcionados de un profesional")
    @ApiResponse(responseCode = "200", description = "Profesional parcialmente actualizado")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    public ResponseEntity<ProfessionalResponse> partialUpdateProfessional(@PathVariable Long id, @RequestBody ProfessionalRequest request) {
        Professional domain = dtoMapper.toDomain(request);
        domain.setId(id);
        return professionalService
            .partialUpdateProfessional(domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar profesional", description = "Elimina un profesional por su ID")
    @ApiResponse(responseCode = "204", description = "Profesional eliminado")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }
}
