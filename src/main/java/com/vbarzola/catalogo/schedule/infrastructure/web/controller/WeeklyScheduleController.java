package com.vbarzola.catalogo.schedule.infrastructure.web.controller;

import com.vbarzola.catalogo.schedule.application.exception.WeeklyScheduleException;
import com.vbarzola.catalogo.schedule.application.service.WeeklyScheduleService;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleRequest;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleResponse;
import com.vbarzola.catalogo.schedule.infrastructure.web.mapper.WeeklyScheduleDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/api/weekly-schedules")
@RequiredArgsConstructor
@Tag(name = "Weekly Schedules", description = "Gestión de horarios semanales de profesionales")
public class WeeklyScheduleController {

    private final WeeklyScheduleService weeklyScheduleService;
    private final WeeklyScheduleDtoMapper dtoMapper;

    @PostMapping
    @Operation(summary = "Crear horario semanal", description = "Guarda un nuevo horario semanal para un profesional")
    @ApiResponse(responseCode = "201", description = "Horario creado exitosamente")
    public ResponseEntity<WeeklyScheduleResponse> createWeeklySchedule(@Valid @RequestBody WeeklyScheduleRequest request)
        throws URISyntaxException {
        WeeklySchedule domain = dtoMapper.toDomain(request);
        WeeklySchedule created = weeklyScheduleService.createWeeklySchedule(domain);
        return ResponseEntity.created(new URI("/api/weekly-schedules/" + created.getId())).body(dtoMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario semanal por ID", description = "Retorna un horario específico con sus relaciones")
    @ApiResponse(responseCode = "200", description = "Horario encontrado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleResponse> getWeeklySchedule(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dtoMapper.toResponse(weeklyScheduleService.getWeeklyScheduleById(id)));
        } catch (WeeklyScheduleException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Listar horarios semanales", description = "Retorna la lista de horarios semanales con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de horarios")
    public ResponseEntity<List<WeeklyScheduleResponse>> getAllWeeklySchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        Page<WeeklySchedule> page = eagerload
            ? weeklyScheduleService.getAllWeeklySchedulesWithEagerRelationships(pageable)
            : weeklyScheduleService.getAllWeeklySchedules(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.map(dtoMapper::toResponse).getContent());
    }

    @GetMapping("/by-professional")
    @Operation(summary = "Obtener horarios por profesional", description = "Retorna todos los horarios semanales de un profesional")
    @ApiResponse(responseCode = "200", description = "Lista de horarios del profesional")
    public ResponseEntity<List<WeeklyScheduleResponse>> getByProfessional(@RequestParam Long professionalId) {
        List<WeeklyScheduleResponse> responses = weeklyScheduleService
            .getByProfessionalId(professionalId)
            .stream()
            .map(dtoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario semanal", description = "Actualiza completamente un horario existente")
    @ApiResponse(responseCode = "200", description = "Horario actualizado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleResponse> updateWeeklySchedule(
        @PathVariable Long id,
        @Valid @RequestBody WeeklyScheduleRequest request
    ) {
        WeeklySchedule domain = dtoMapper.toDomain(request);
        return weeklyScheduleService
            .updateWeeklySchedule(id, domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(summary = "Actualizar parcialmente horario semanal", description = "Actualiza solo los campos proporcionados de un horario")
    @ApiResponse(responseCode = "200", description = "Horario parcialmente actualizado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleResponse> partialUpdateWeeklySchedule(
        @PathVariable Long id,
        @RequestBody WeeklyScheduleRequest request
    ) {
        WeeklySchedule domain = dtoMapper.toDomain(request);
        domain.setId(id);
        return weeklyScheduleService
            .partialUpdateWeeklySchedule(domain)
            .map(dtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar horario semanal", description = "Elimina un horario por su ID")
    @ApiResponse(responseCode = "204", description = "Horario eliminado")
    public ResponseEntity<Void> deleteWeeklySchedule(@PathVariable Long id) {
        weeklyScheduleService.deleteWeeklySchedule(id);
        return ResponseEntity.noContent().build();
    }
}
