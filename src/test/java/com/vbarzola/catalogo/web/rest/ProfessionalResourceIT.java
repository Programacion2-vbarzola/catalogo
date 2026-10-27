package com.vbarzola.catalogo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.IntegrationTest;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalCategoryEntity;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalRepository;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalRequest;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link com.vbarzola.catalogo.professional.infrastructure.web.controller.ProfessionalController}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/professionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JpaProfessionalRepository professionalRepository;

    @Autowired
    private JpaProfessionalCategoryRepository professionalCategoryRepository;

    @Autowired
    private MockMvc restProfessionalMockMvc;

    private ProfessionalCategoryEntity category;

    private ProfessionalEntity insertedProfessional;

    private static ProfessionalCategoryEntity createCategoryEntity() {
        return ProfessionalCategoryEntity.builder()
            .id(longCount.incrementAndGet())
            .name("Category")
            .description("Description")
            .enabled(true)
            .createdAt(Instant.ofEpochMilli(0L))
            .updatedAt(Instant.ofEpochMilli(0L))
            .build();
    }

    private ProfessionalEntity createEntity() {
        return ProfessionalEntity.builder()
            .id(longCount.incrementAndGet())
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .enabled(DEFAULT_ENABLED)
            .createdAt(Instant.ofEpochMilli(0L))
            .updatedAt(Instant.ofEpochMilli(0L))
            .category(category)
            .build();
    }

    @BeforeEach
    void initTest() {
        category = professionalCategoryRepository.saveAndFlush(createCategoryEntity());
        insertedProfessional = null;
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessional != null) {
            professionalRepository.deleteById(insertedProfessional.getId());
            insertedProfessional = null;
        }
        professionalCategoryRepository.deleteById(category.getId());
    }

    @Test
    @Transactional
    void createProfessional() throws Exception {
        long databaseSizeBeforeCreate = professionalRepository.count();

        ProfessionalRequest request = ProfessionalRequest.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .enabled(DEFAULT_ENABLED)
            .categoryId(category.getId())
            .build();

        var response = restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long createdId = om.readTree(response).get("id").asLong();
        assertThat(professionalRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
        insertedProfessional = professionalRepository.findById(createdId).orElseThrow();
        assertThat(insertedProfessional.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(insertedProfessional.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(insertedProfessional.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = professionalRepository.count();
        ProfessionalRequest request = ProfessionalRequest.builder()
            .lastName(DEFAULT_LAST_NAME)
            .enabled(DEFAULT_ENABLED)
            .categoryId(category.getId())
            .build();

        restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        assertThat(professionalRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionals() throws Exception {
        insertedProfessional = professionalRepository.saveAndFlush(createEntity());

        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedProfessional.getId().intValue())));
    }

    @Test
    @Transactional
    void getProfessional() throws Exception {
        insertedProfessional = professionalRepository.saveAndFlush(createEntity());

        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL_ID, insertedProfessional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insertedProfessional.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.category.id").value(category.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProfessional() throws Exception {
        restProfessionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessional() throws Exception {
        insertedProfessional = professionalRepository.saveAndFlush(createEntity());

        ProfessionalRequest request = ProfessionalRequest.builder()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .enabled(UPDATED_ENABLED)
            .categoryId(category.getId())
            .build();

        restProfessionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insertedProfessional.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(UPDATED_FIRST_NAME));

        ProfessionalEntity updated = professionalRepository.findById(insertedProfessional.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(updated.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProfessional() throws Exception {
        ProfessionalRequest request = ProfessionalRequest.builder()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .enabled(DEFAULT_ENABLED)
            .categoryId(category.getId())
            .build();

        restProfessionalMockMvc
            .perform(put(ENTITY_API_URL_ID, Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void partialUpdateProfessionalWithPatch() throws Exception {
        insertedProfessional = professionalRepository.saveAndFlush(createEntity());

        ProfessionalRequest request = ProfessionalRequest.builder().firstName(UPDATED_FIRST_NAME).build();

        restProfessionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insertedProfessional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk());

        ProfessionalEntity updated = professionalRepository.findById(insertedProfessional.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(updated.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(updated.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    @Transactional
    void deleteProfessional() throws Exception {
        insertedProfessional = professionalRepository.saveAndFlush(createEntity());
        long databaseSizeBeforeDelete = professionalRepository.count();

        restProfessionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, insertedProfessional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(professionalRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
        insertedProfessional = null;
    }
}
