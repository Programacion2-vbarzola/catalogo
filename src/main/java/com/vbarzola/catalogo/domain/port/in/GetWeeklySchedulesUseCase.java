package com.vbarzola.catalogo.domain.port.in;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import java.util.List;

public interface GetWeeklySchedulesUseCase {
    List<WeeklyScheduleDTO> getByProfessionalId(Long professionalId);
}
