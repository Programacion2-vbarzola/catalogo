package com.vbarzola.catalogo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeeklyScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyScheduleDTO.class);
        WeeklyScheduleDTO weeklyScheduleDTO1 = new WeeklyScheduleDTO();
        weeklyScheduleDTO1.setId(1L);
        WeeklyScheduleDTO weeklyScheduleDTO2 = new WeeklyScheduleDTO();
        assertThat(weeklyScheduleDTO1).isNotEqualTo(weeklyScheduleDTO2);
        weeklyScheduleDTO2.setId(weeklyScheduleDTO1.getId());
        assertThat(weeklyScheduleDTO1).isEqualTo(weeklyScheduleDTO2);
        weeklyScheduleDTO2.setId(2L);
        assertThat(weeklyScheduleDTO1).isNotEqualTo(weeklyScheduleDTO2);
        weeklyScheduleDTO1.setId(null);
        assertThat(weeklyScheduleDTO1).isNotEqualTo(weeklyScheduleDTO2);
    }
}
