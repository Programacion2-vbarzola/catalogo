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
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.entity.WeeklyScheduleEntity;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.repository.JpaWeeklyScheduleRepository;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleRequest;
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
 * Integration tests for the {@link com.vbarzola.catalogo.schedule.infrastructure.web.controller.WeeklyScheduleController}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeeklyScheduleResourceIT {

    private static final CatalogDayOfWeek DEFAULT_DAY_OF_WEEK = CatalogDayOfWeek.MONDAY;
    private static final CatalogDayOfWeek UPDATED_DAY_OF_WEEK = CatalogDayOfWeek.TUESDAY;

    private static final String DEFAULT_START_TIME = "AAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBB";

    private static final Integer DEFAULT_SLOT_DURATION_MINUTES = 1;
    private static final Integer UPDATED_SLOT_DURATION_MINUTES = 2;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/weekly-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JpaWeeklyScheduleRepository weeklyScheduleRepository;

    @Autowired
    private JpaProfessionalRepository professionalRepository;

    @Autowired
    private JpaProfessionalCategoryRepository professionalCategoryRepository;

    @Autowired
    private MockMvc restWeeklyScheduleMockMvc;

    private ProfessionalCategoryEntity category;

    private ProfessionalEntity professional;

    private WeeklyScheduleEntity insertedWeeklySchedule;

    private WeeklyScheduleEntity createEntity() {
        return WeeklyScheduleEntity.builder()
            .id(longCount.incrementAndGet())
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .slotDurationMinutes(DEFAULT_SLOT_DURATION_MINUTES)
            .enabled(DEFAULT_ENABLED)
            .createdAt(Instant.ofEpochMilli(0L))
            .updatedAt(Instant.ofEpochMilli(0L))
            .professional(professional)
            .build();
    }

    @BeforeEach
    void initTest() {
        category = professionalCategoryRepository.saveAndFlush(
            ProfessionalCategoryEntity.builder()
                .id(longCount.incrementAndGet())
                .name("Category")
                .description("Description")
                .enabled(true)
                .build()
        );
        professional = professionalRepository.saveAndFlush(
            ProfessionalEntity.builder()
                .id(longCount.incrementAndGet())
                .firstName("FirstName")
                .lastName("LastName")
                .enabled(true)
                .category(category)
                .build()
        );
        insertedWeeklySchedule = null;
    }

    @AfterEach
    void cleanup() {
        if (insertedWeeklySchedule != null) {
            weeklyScheduleRepository.deleteById(insertedWeeklySchedule.getId());
            insertedWeeklySchedule = null;
        }
        professionalRepository.deleteById(professional.getId());
        professionalCategoryRepository.deleteById(category.getId());
    }

    @Test
    @Transactional
    void createWeeklySchedule() throws Exception {
        long databaseSizeBeforeCreate = weeklyScheduleRepository.count();

        WeeklyScheduleRequest request = WeeklyScheduleRequest.builder()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .slotDurationMinutes(DEFAULT_SLOT_DURATION_MINUTES)
            .enabled(DEFAULT_ENABLED)
            .professionalId(professional.getId())
            .build();

        var response = restWeeklyScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long createdId = om.readTree(response).get("id").asLong();
        assertThat(weeklyScheduleRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
        insertedWeeklySchedule = weeklyScheduleRepository.findById(createdId).orElseThrow();
        assertThat(insertedWeeklySchedule.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(insertedWeeklySchedule.getProfessional().getId()).isEqualTo(professional.getId());
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = weeklyScheduleRepository.count();
        WeeklyScheduleRequest request = WeeklyScheduleRequest.builder()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .endTime(DEFAULT_END_TIME)
            .slotDurationMinutes(DEFAULT_SLOT_DURATION_MINUTES)
            .enabled(DEFAULT_ENABLED)
            .professionalId(professional.getId())
            .build();

        restWeeklyScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        assertThat(weeklyScheduleRepository.count()).isEqualTo(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeeklySchedules() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());

        restWeeklyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?eagerload=false"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedWeeklySchedule.getId().intValue())));
    }

    @Test
    @Transactional
    void getByProfessional() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());

        restWeeklyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/by-professional").param("professionalId", professional.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedWeeklySchedule.getId().intValue())));
    }

    @Test
    @Transactional
    void getWeeklySchedule() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());

        restWeeklyScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, insertedWeeklySchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insertedWeeklySchedule.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.professional.id").value(professional.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWeeklySchedule() throws Exception {
        restWeeklyScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeeklySchedule() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());

        WeeklyScheduleRequest request = WeeklyScheduleRequest.builder()
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .slotDurationMinutes(UPDATED_SLOT_DURATION_MINUTES)
            .enabled(UPDATED_ENABLED)
            .professionalId(professional.getId())
            .build();

        restWeeklyScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insertedWeeklySchedule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.startTime").value(UPDATED_START_TIME));

        WeeklyScheduleEntity updated = weeklyScheduleRepository.findById(insertedWeeklySchedule.getId()).orElseThrow();
        assertThat(updated.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(updated.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void putNonExistingWeeklySchedule() throws Exception {
        WeeklyScheduleRequest request = WeeklyScheduleRequest.builder()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .slotDurationMinutes(DEFAULT_SLOT_DURATION_MINUTES)
            .enabled(DEFAULT_ENABLED)
            .professionalId(professional.getId())
            .build();

        restWeeklyScheduleMockMvc
            .perform(put(ENTITY_API_URL_ID, Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void partialUpdateWeeklyScheduleWithPatch() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());

        WeeklyScheduleRequest request = WeeklyScheduleRequest.builder().startTime(UPDATED_START_TIME).build();

        restWeeklyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insertedWeeklySchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isOk());

        WeeklyScheduleEntity updated = weeklyScheduleRepository.findById(insertedWeeklySchedule.getId()).orElseThrow();
        assertThat(updated.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(updated.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(updated.getProfessional().getId()).isEqualTo(professional.getId());
    }

    @Test
    @Transactional
    void deleteWeeklySchedule() throws Exception {
        insertedWeeklySchedule = weeklyScheduleRepository.saveAndFlush(createEntity());
        long databaseSizeBeforeDelete = weeklyScheduleRepository.count();

        restWeeklyScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, insertedWeeklySchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(weeklyScheduleRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
        insertedWeeklySchedule = null;
    }
}
