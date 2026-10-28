package com.vbarzola.catalogo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.IntegrationTest;
import com.vbarzola.catalogo.user.domain.model.Authority;
import com.vbarzola.catalogo.user.infrastructure.persistence.entity.AuthorityEntity;
import com.vbarzola.catalogo.user.infrastructure.persistence.repository.JpaAuthorityRepository;
import java.util.UUID;
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
 * Integration tests for the {@link com.vbarzola.catalogo.user.infrastructure.web.controller.AuthorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = { "ROLE_ADMIN" })
class AuthorityResourceIT {

    private static final String ENTITY_API_URL = "/api/authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{name}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JpaAuthorityRepository authorityRepository;

    @Autowired
    private MockMvc restAuthorityMockMvc;

    private String insertedAuthorityName;

    @BeforeEach
    void initTest() {
        insertedAuthorityName = null;
    }

    @AfterEach
    void cleanup() {
        if (insertedAuthorityName != null) {
            authorityRepository.deleteById(insertedAuthorityName);
            insertedAuthorityName = null;
        }
    }

    @Test
    @Transactional
    void createAuthority() throws Exception {
        long databaseSizeBeforeCreate = authorityRepository.count();
        Authority authority = Authority.builder().name(UUID.randomUUID().toString()).build();

        var response = restAuthorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authority)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        insertedAuthorityName = om.readTree(response).get("name").asText();
        assertThat(authorityRepository.count()).isEqualTo(databaseSizeBeforeCreate + 1);
        assertThat(authorityRepository.findById(insertedAuthorityName)).isPresent();
    }

    @Test
    @Transactional
    void createAuthorityWithExistingId() throws Exception {
        String name = UUID.randomUUID().toString();
        insertedAuthorityName = authorityRepository.saveAndFlush(new AuthorityEntity().name(name)).getName();

        long databaseSizeBeforeCreate = authorityRepository.count();

        Authority authority = Authority.builder().name(name).build();
        restAuthorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authority)))
            .andExpect(status().isBadRequest());

        assertThat(authorityRepository.count()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        String name = UUID.randomUUID().toString();
        insertedAuthorityName = authorityRepository.saveAndFlush(new AuthorityEntity().name(name)).getName();

        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=name,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].name").value(hasItem(name)));
    }

    @Test
    @Transactional
    void getAuthority() throws Exception {
        String name = UUID.randomUUID().toString();
        insertedAuthorityName = authorityRepository.saveAndFlush(new AuthorityEntity().name(name)).getName();

        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL_ID, name))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    @Transactional
    void getNonExistingAuthority() throws Exception {
        restAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, "does-not-exist")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteAuthority() throws Exception {
        String name = UUID.randomUUID().toString();
        insertedAuthorityName = authorityRepository.saveAndFlush(new AuthorityEntity().name(name)).getName();

        long databaseSizeBeforeDelete = authorityRepository.count();

        restAuthorityMockMvc.perform(delete(ENTITY_API_URL_ID, name).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        assertThat(authorityRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
        insertedAuthorityName = null;
    }
}
