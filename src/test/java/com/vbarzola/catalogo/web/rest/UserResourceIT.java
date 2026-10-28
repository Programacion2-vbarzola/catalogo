package com.vbarzola.catalogo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.IntegrationTest;
import com.vbarzola.catalogo.security.AuthoritiesConstants;
import com.vbarzola.catalogo.user.application.dto.AdminUserDTO;
import com.vbarzola.catalogo.user.application.service.UserService;
import com.vbarzola.catalogo.user.infrastructure.persistence.entity.UserEntity;
import com.vbarzola.catalogo.user.infrastructure.persistence.repository.JpaUserRepository;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
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
 * Integration tests for the {@link com.vbarzola.catalogo.user.infrastructure.web.controller.UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc restUserMockMvc;

    private UserEntity user;

    private Long numberOfUsers;

    /**
     * Setups the database with one user.
     */
    public static UserEntity initTestUser() {
        UserEntity persistUser = new UserEntity();
        persistUser.setLogin(DEFAULT_LOGIN);
        persistUser.setPassword(RandomStringUtils.insecure().nextAlphanumeric(60));
        persistUser.setActivated(true);
        persistUser.setEmail(DEFAULT_EMAIL);
        persistUser.setFirstName(DEFAULT_FIRSTNAME);
        persistUser.setLastName(DEFAULT_LASTNAME);
        persistUser.setImageUrl(DEFAULT_IMAGEURL);
        persistUser.setLangKey(DEFAULT_LANGKEY);
        return persistUser;
    }

    @BeforeEach
    void countUsers() {
        numberOfUsers = userRepository.count();
    }

    @BeforeEach
    void initTest() {
        user = initTestUser();
    }

    @AfterEach
    void cleanupAndCheck() {
        userService.deleteUser(DEFAULT_LOGIN);
        userService.deleteUser("anotherlogin");
        assertThat(userRepository.count()).isEqualTo(numberOfUsers);
        numberOfUsers = null;
    }

    private AdminUserDTO newUserDTO() {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));
        return userDTO;
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        AdminUserDTO userDTO = newUserDTO();

        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isCreated());

        assertThat(userRepository.findOneByLogin(DEFAULT_LOGIN)).isPresent();
    }

    @Test
    @Transactional
    void createUserWithExistingLogin() throws Exception {
        userRepository.saveAndFlush(user);
        long databaseSizeBeforeCreate = userRepository.count();

        AdminUserDTO userDTO = newUserDTO();
        userDTO.setEmail("anothermail@localhost");

        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void createUserWithExistingEmail() throws Exception {
        userRepository.saveAndFlush(user);
        long databaseSizeBeforeCreate = userRepository.count();

        AdminUserDTO userDTO = newUserDTO();
        userDTO.setLogin("anotherlogin");

        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        userRepository.saveAndFlush(user);

        restUserMockMvc
            .perform(get("/api/admin/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        userRepository.saveAndFlush(user);

        restUserMockMvc
            .perform(get("/api/admin/users/{login}", user.getLogin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/admin/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        UserEntity savedUser = userRepository.saveAndFlush(user);

        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setLogin(savedUser.getLogin());
        userDTO.setFirstName(UPDATED_FIRSTNAME);
        userDTO.setLastName(UPDATED_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(UPDATED_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isOk());

        UserEntity updated = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(updated.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(updated.getLangKey()).isEqualTo(UPDATED_LANGKEY);
    }

    @Test
    @Transactional
    void updateUserExistingEmail() throws Exception {
        UserEntity savedUser = userRepository.saveAndFlush(user);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.insecure().nextAlphanumeric(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setLogin(savedUser.getLogin());
        userDTO.setFirstName(savedUser.getFirstName());
        userDTO.setLastName(savedUser.getLastName());
        userDTO.setEmail("jhipster@localhost"); // already used by anotherUser
        userDTO.setActivated(true);
        userDTO.setImageUrl(savedUser.getImageUrl());
        userDTO.setLangKey(savedUser.getLangKey());
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        userService.deleteUser("jhipster");
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        userRepository.saveAndFlush(user);
        long databaseSizeBeforeDelete = userRepository.count();

        restUserMockMvc
            .perform(delete("/api/admin/users/{login}", user.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(userRepository.count()).isEqualTo(databaseSizeBeforeDelete - 1);
    }
}
