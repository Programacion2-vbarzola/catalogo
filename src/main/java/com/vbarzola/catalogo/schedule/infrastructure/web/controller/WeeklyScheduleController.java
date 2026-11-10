package com.vbarzola.catalogo.schedule.infrastructure.web.controller;

import com.vbarzola.catalogo.schedule.application.exception.WeeklyScheduleException;
import com.vbarzola.catalogo.schedule.application.service.WeeklyScheduleService;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleResponse;
import com.vbarzola.catalogo.schedule.infrastructure.web.mapper.WeeklyScheduleDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Weekly Schedules", description = "Consulta de horarios semanales de profesionales")
public class WeeklyScheduleController {

    private final WeeklyScheduleService weeklyScheduleService;
    private final WeeklyScheduleDtoMapper dtoMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario semanal por ID")
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
    @Operation(summary = "Listar horarios semanales con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de horarios")
    public ResponseEntity<List<WeeklyScheduleResponse>> getAllWeeklySchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        Page<WeeklyScheduleResponse> page = eagerload
            ? weeklyScheduleService.getAllWeeklySchedulesWithEagerRelationships(pageable).map(dtoMapper::toResponse)
            : weeklyScheduleService.getAllWeeklySchedules(pageable).map(dtoMapper::toResponse);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/by-professional")
    @Operation(summary = "Obtener horarios de un profesional")
    @ApiResponse(responseCode = "200", description = "Lista de horarios del profesional")
    public ResponseEntity<List<WeeklyScheduleResponse>> getByProfessional(@RequestParam Long professionalId) {
        List<WeeklyScheduleResponse> responses = weeklyScheduleService
            .getByProfessionalId(professionalId)
            .stream()
            .map(dtoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
