package com.vbarzola.catalogo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.IntegrationTest;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalCategoryEntity;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link com.vbarzola.catalogo.professional.infrastructure.web.controller.ProfessionalCategoryController}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/professional-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JpaProfessionalCategoryRepository professionalCategoryRepository;

    @Autowired
    private MockMvc restProfessionalCategoryMockMvc;

    private ProfessionalCategoryEntity insertedProfessionalCategory;

    public static ProfessionalCategoryRequest createRequest() {
        return ProfessionalCategoryRequest.builder().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).enabled(DEFAULT_ENABLED).build();
    }

    public static ProfessionalCategoryEntity createEntity() {
        return ProfessionalCategoryEntity.builder()
            .id(longCount.incrementAndGet())
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .enabled(DEFAULT_ENABLED)
            .createdAt(Instant.ofEpochMilli(0L))
            .updatedAt(Instant.ofEpochMilli(0L))
            .build();
    }

    @BeforeEach
    void initTest() {
        insertedProfessionalCategory = null;
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalCategory != null) {
            professionalCategoryRepository.deleteById(insertedProfessionalCategory.getId());
            insertedProfessionalCategory = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalCategory() throws Exception {
        long databaseSizeBeforeCreate = professionalCategoryRepository.count();

        ProfessionalCategoryRequest request = createRequest();
        var response = restProfessionalCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long createdId = om.readTree(response).get("id").asLong();
        assertThat(professionalCategoryRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
        insertedProfessionalCategory = professionalCategoryRepository.findById(createdId).orElseThrow();
        assertThat(insertedProfessionalCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(insertedProfessionalCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(insertedProfessionalCategory.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = professionalCategoryRepository.count();
        ProfessionalCategoryRequest request = ProfessionalCategoryRequest.builder()
            .description(DEFAULT_DESCRIPTION)
            .enabled(DEFAULT_ENABLED)
            .build();

        restProfessionalCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        assertThat(professionalCategoryRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalCategories() throws Exception {
        insertedProfessionalCategory = professionalCategoryRepository.saveAndFlush(createEntity());

        restProfessionalCategoryMockMvc
            .perform(get(ENTITY_API_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedProfessionalCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProfessionalCategory() throws Exception {
        insertedProfessionalCategory = professionalCategoryRepository.saveAndFlush(createEntity());

        restProfessionalCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, insertedProfessionalCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insertedProfessionalCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalCategory() throws Exception {
        restProfessionalCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalCategory() throws Exception {
        insertedProfessionalCategory = professionalCategoryRepository.saveAndFlush(createEntity());

        ProfessionalCategoryRequest request = ProfessionalCategoryRequest.builder()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .build();

        restProfessionalCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insertedProfessionalCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(UPDATED_NAME));

        ProfessionalCategoryEntity updated = professionalCategoryRepository.findById(insertedProfessionalCategory.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo(UPDATED_NAME);
        assertThat(updated.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalCategory() throws Exception {
        ProfessionalCategoryRequest request = createRequest();

        restProfessionalCategoryMockMvc
            .perform(put(ENTITY_API_URL_ID, Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void partialUpdateProfessionalCategoryWithPatch() throws Exception {
        insertedProfessionalCategory = professionalCategoryRepository.saveAndFlush(createEntity());

        ProfessionalCategoryRequest request = ProfessionalCategoryRequest.builder()
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .build();

        restProfessionalCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insertedProfessionalCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk());

        ProfessionalCategoryEntity updated = professionalCategoryRepository.findById(insertedProfessionalCategory.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(updated.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(updated.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void deleteProfessionalCategory() throws Exception {
        insertedProfessionalCategory = professionalCategoryRepository.saveAndFlush(createEntity());
        long databaseSizeBeforeDelete = professionalCategoryRepository.count();

        restProfessionalCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, insertedProfessionalCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(professionalCategoryRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
        insertedProfessionalCategory = null;
    }
}
