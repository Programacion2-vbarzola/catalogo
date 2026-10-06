package ar.edu.um.programacion2.catalogo.web.rest;

import ar.edu.um.programacion2.catalogo.repository.WeeklyScheduleRepository;
import ar.edu.um.programacion2.catalogo.service.WeeklyScheduleService;
import ar.edu.um.programacion2.catalogo.service.dto.WeeklyScheduleDTO;
import ar.edu.um.programacion2.catalogo.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ar.edu.um.programacion2.catalogo.domain.WeeklySchedule}.
 */
@RestController
@RequestMapping("/api/weekly-schedules")
public class WeeklyScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklyScheduleResource.class);

    private static final String ENTITY_NAME = "weeklySchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeeklyScheduleService weeklyScheduleService;

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public WeeklyScheduleResource(WeeklyScheduleService weeklyScheduleService, WeeklyScheduleRepository weeklyScheduleRepository) {
        this.weeklyScheduleService = weeklyScheduleService;
        this.weeklyScheduleRepository = weeklyScheduleRepository;
    }

    /**
     * {@code POST  /weekly-schedules} : Create a new weeklySchedule.
     *
     * @param weeklyScheduleDTO the weeklyScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weeklyScheduleDTO, or with status {@code 400 (Bad Request)} if the weeklySchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WeeklyScheduleDTO> createWeeklySchedule(@Valid @RequestBody WeeklyScheduleDTO weeklyScheduleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WeeklySchedule : {}", weeklyScheduleDTO);
        if (weeklyScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklySchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        weeklyScheduleDTO = weeklyScheduleService.save(weeklyScheduleDTO);
        return ResponseEntity.created(new URI("/api/weekly-schedules/" + weeklyScheduleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString()))
            .body(weeklyScheduleDTO);
    }

    /**
     * {@code PUT  /weekly-schedules/:id} : Updates an existing weeklySchedule.
     *
     * @param id the id of the weeklyScheduleDTO to save.
     * @param weeklyScheduleDTO the weeklyScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weeklyScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
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

        if (!weeklyScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        weeklyScheduleDTO = weeklyScheduleService.update(weeklyScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString()))
            .body(weeklyScheduleDTO);
    }

    /**
     * {@code PATCH  /weekly-schedules/:id} : Partial updates given fields of an existing weeklySchedule, field will ignore if it is null
     *
     * @param id the id of the weeklyScheduleDTO to save.
     * @param weeklyScheduleDTO the weeklyScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weeklyScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weeklyScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeeklyScheduleDTO> partialUpdateWeeklySchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeeklyScheduleDTO weeklyScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WeeklySchedule partially : {}, {}", id, weeklyScheduleDTO);
        if (weeklyScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeeklyScheduleDTO> result = weeklyScheduleService.partialUpdate(weeklyScheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyScheduleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weekly-schedules} : get all the weeklySchedules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weeklySchedules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WeeklyScheduleDTO>> getAllWeeklySchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of WeeklySchedules");
        Page<WeeklyScheduleDTO> page;
        if (eagerload) {
            page = weeklyScheduleService.findAllWithEagerRelationships(pageable);
        } else {
            page = weeklyScheduleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weekly-schedules/:id} : get the "id" weeklySchedule.
     *
     * @param id the id of the weeklyScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weeklyScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeeklyScheduleDTO> getWeeklySchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WeeklySchedule : {}", id);
        Optional<WeeklyScheduleDTO> weeklyScheduleDTO = weeklyScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyScheduleDTO);
    }

    /**
     * {@code DELETE  /weekly-schedules/:id} : delete the "id" weeklySchedule.
     *
     * @param id the id of the weeklyScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeeklySchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WeeklySchedule : {}", id);
        weeklyScheduleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
