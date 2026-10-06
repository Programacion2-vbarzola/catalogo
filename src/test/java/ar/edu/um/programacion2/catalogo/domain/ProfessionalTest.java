package ar.edu.um.programacion2.catalogo.domain;

import static ar.edu.um.programacion2.catalogo.domain.ProfessionalCategoryTestSamples.*;
import static ar.edu.um.programacion2.catalogo.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.catalogo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professional.class);
        Professional professional1 = getProfessionalSample1();
        Professional professional2 = new Professional();
        assertThat(professional1).isNotEqualTo(professional2);

        professional2.setId(professional1.getId());
        assertThat(professional1).isEqualTo(professional2);

        professional2 = getProfessionalSample2();
        assertThat(professional1).isNotEqualTo(professional2);
    }

    @Test
    void categoryTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalCategory professionalCategoryBack = getProfessionalCategoryRandomSampleGenerator();

        professional.setCategory(professionalCategoryBack);
        assertThat(professional.getCategory()).isEqualTo(professionalCategoryBack);

        professional.category(null);
        assertThat(professional.getCategory()).isNull();
    }
}
