package com.vbarzola.catalogo.adapter.in.rest;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import com.vbarzola.catalogo.domain.port.in.ProfessionalUseCase;
import com.vbarzola.catalogo.web.rest.errors.BadRequestAlertException;
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
@RequestMapping("/api/professionals")
public class ProfessionalResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalResource.class);
    private static final String ENTITY_NAME = "professional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalUseCase useCase;

    public ProfessionalResource(ProfessionalUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("")
    public ResponseEntity<ProfessionalDTO> createProfessional(@Valid @RequestBody ProfessionalDTO professionalDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Professional : {}", professionalDTO);
        if (professionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new professional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalDTO = useCase.save(professionalDTO);
        return ResponseEntity.created(new URI("/api/professionals/" + professionalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, professionalDTO.getId().toString()))
            .body(professionalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalDTO> updateProfessional(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalDTO professionalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Professional : {}, {}", id, professionalDTO);
        if (professionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        professionalDTO = useCase.update(professionalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalDTO.getId().toString()))
            .body(professionalDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalDTO> partialUpdateProfessional(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalDTO professionalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Professional : {}, {}", id, professionalDTO);
        if (professionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<ProfessionalDTO> result = useCase.partialUpdate(professionalDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfessionalDTO>> getAllProfessionals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Professionals");
        Page<ProfessionalDTO> page = eagerload ? useCase.findAllWithEagerRelationships(pageable) : useCase.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalDTO> getProfessional(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Professional : {}", id);
        return ResponseUtil.wrapOrNotFound(useCase.findOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Professional : {}", id);
        useCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
