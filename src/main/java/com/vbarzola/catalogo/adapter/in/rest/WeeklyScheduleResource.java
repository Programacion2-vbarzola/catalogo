package com.vbarzola.catalogo.adapter.in.rest;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.domain.port.in.GetWeeklySchedulesUseCase;
import com.vbarzola.catalogo.domain.port.in.WeeklyScheduleUseCase;
import com.vbarzola.catalogo.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/weekly-schedules")
@Tag(name = "Weekly Schedules", description = "Gestión de horarios semanales de profesionales")
public class WeeklyScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklyScheduleResource.class);
    private static final String ENTITY_NAME = "weeklySchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeeklyScheduleUseCase useCase;
    private final GetWeeklySchedulesUseCase getSchedulesUseCase;

    public WeeklyScheduleResource(WeeklyScheduleUseCase useCase, GetWeeklySchedulesUseCase getSchedulesUseCase) {
        this.useCase = useCase;
        this.getSchedulesUseCase = getSchedulesUseCase;
    }

    @PostMapping("")
    @Operation(summary = "Crear horario semanal", description = "Guarda un nuevo horario semanal para un profesional")
    @ApiResponse(responseCode = "201", description = "Horario creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<WeeklyScheduleDTO> createWeeklySchedule(@Valid @RequestBody WeeklyScheduleDTO weeklyScheduleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WeeklySchedule : {}", weeklyScheduleDTO);
        if (weeklyScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklySchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        weeklyScheduleDTO = useCase.save(weeklyScheduleDTO);
        return ResponseEntity.created(new URI("/api/weekly-schedules/" + weeklyScheduleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString()))
            .body(weeklyScheduleDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario semanal", description = "Actualiza completamente un horario existente")
    @ApiResponse(responseCode = "200", description = "Horario actualizado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleDTO> updateWeeklySchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WeeklyScheduleDTO weeklyScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WeeklySchedule : {}, {}", id, weeklyScheduleDTO);
        if (weeklyScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        weeklyScheduleDTO = useCase.update(weeklyScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString()))
            .body(weeklyScheduleDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(summary = "Actualizar parcialmente horario semanal", description = "Actualiza solo los campos proporcionados de un horario")
    @ApiResponse(responseCode = "200", description = "Horario parcialmente actualizado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleDTO> partialUpdateWeeklySchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeeklyScheduleDTO weeklyScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WeeklySchedule : {}, {}", id, weeklyScheduleDTO);
        if (weeklyScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<WeeklyScheduleDTO> result = useCase.partialUpdate(weeklyScheduleDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString())
        );
    }

    @GetMapping("")
    @Operation(summary = "Listar horarios semanales", description = "Retorna la lista de horarios semanales con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de horarios")
    public ResponseEntity<List<WeeklyScheduleDTO>> getAllWeeklySchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of WeeklySchedules");
        Page<WeeklyScheduleDTO> page = eagerload ? useCase.findAllWithEagerRelationships(pageable) : useCase.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/by-professional")
    @Operation(summary = "Obtener horarios por profesional", description = "Retorna todos los horarios semanales de un profesional")
    @ApiResponse(responseCode = "200", description = "Lista de horarios del profesional")
    public ResponseEntity<List<WeeklyScheduleDTO>> getByProfessional(@RequestParam Long professionalId) {
        LOG.debug("REST request to get WeeklySchedules for professionalId={}", professionalId);
        return ResponseEntity.ok(getSchedulesUseCase.getByProfessionalId(professionalId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario semanal por ID", description = "Retorna un horario específico con sus relaciones")
    @ApiResponse(responseCode = "200", description = "Horario encontrado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<WeeklyScheduleDTO> getWeeklySchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WeeklySchedule : {}", id);
        return ResponseUtil.wrapOrNotFound(useCase.findOne(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar horario semanal", description = "Elimina un horario por su ID")
    @ApiResponse(responseCode = "204", description = "Horario eliminado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    public ResponseEntity<Void> deleteWeeklySchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WeeklySchedule : {}", id);
        useCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
