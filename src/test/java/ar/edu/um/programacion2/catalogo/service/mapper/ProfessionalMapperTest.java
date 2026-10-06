package ar.edu.um.programacion2.catalogo.service.mapper;

import static ar.edu.um.programacion2.catalogo.domain.ProfessionalAsserts.*;
import static ar.edu.um.programacion2.catalogo.domain.ProfessionalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalMapperTest {

    private ProfessionalMapper professionalMapper;

    @BeforeEach
    void setUp() {
        professionalMapper = new ProfessionalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalSample1();
        var actual = professionalMapper.toEntity(professionalMapper.toDto(expected));
        assertProfessionalAllPropertiesEquals(expected, actual);
    }
}
