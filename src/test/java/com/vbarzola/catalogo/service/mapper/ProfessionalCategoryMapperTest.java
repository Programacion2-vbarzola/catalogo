package com.vbarzola.catalogo.service.mapper;

import static com.vbarzola.catalogo.domain.ProfessionalCategoryAsserts.*;
import static com.vbarzola.catalogo.domain.ProfessionalCategoryTestSamples.*;

import com.vbarzola.catalogo.domain.mapper.ProfessionalCategoryMapper;
import com.vbarzola.catalogo.domain.mapper.ProfessionalCategoryMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalCategoryMapperTest {

    private ProfessionalCategoryMapper professionalCategoryMapper;

    @BeforeEach
    void setUp() {
        professionalCategoryMapper = new ProfessionalCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalCategorySample1();
        var actual = professionalCategoryMapper.toEntity(professionalCategoryMapper.toDto(expected));
        assertProfessionalCategoryAllPropertiesEquals(expected, actual);
    }
}
