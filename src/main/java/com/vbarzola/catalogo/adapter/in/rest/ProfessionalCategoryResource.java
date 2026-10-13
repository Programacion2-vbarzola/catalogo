package com.vbarzola.catalogo.adapter.in.rest;

import com.vbarzola.catalogo.domain.dto.ProfessionalCategoryDTO;
import com.vbarzola.catalogo.domain.port.in.ProfessionalCategoryUseCase;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/professional-categories")
public class ProfessionalCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalCategoryResource.class);
    private static final String ENTITY_NAME = "professionalCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalCategoryUseCase useCase;

    public ProfessionalCategoryResource(ProfessionalCategoryUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("")
    public ResponseEntity<ProfessionalCategoryDTO> createProfessionalCategory(
        @Valid @RequestBody ProfessionalCategoryDTO professionalCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalCategory : {}", professionalCategoryDTO);
        if (professionalCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalCategoryDTO = useCase.save(professionalCategoryDTO);
        return ResponseEntity.created(new URI("/api/professional-categories/" + professionalCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, professionalCategoryDTO.getId().toString()))
            .body(professionalCategoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalCategoryDTO> updateProfessionalCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalCategoryDTO professionalCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalCategory : {}, {}", id, professionalCategoryDTO);
        if (professionalCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        professionalCategoryDTO = useCase.update(professionalCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalCategoryDTO.getId().toString()))
            .body(professionalCategoryDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalCategoryDTO> partialUpdateProfessionalCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalCategoryDTO professionalCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalCategory : {}, {}", id, professionalCategoryDTO);
        if (professionalCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!useCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<ProfessionalCategoryDTO> result = useCase.partialUpdate(professionalCategoryDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalCategoryDTO.getId().toString())
        );
    }

    @GetMapping("")
    public List<ProfessionalCategoryDTO> getAllProfessionalCategories() {
        LOG.debug("REST request to get all ProfessionalCategories");
        return useCase.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalCategoryDTO> getProfessionalCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalCategory : {}", id);
        return ResponseUtil.wrapOrNotFound(useCase.findOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalCategory : {}", id);
        useCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
