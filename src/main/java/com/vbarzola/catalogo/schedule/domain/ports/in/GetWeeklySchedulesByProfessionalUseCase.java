package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import java.util.List;

public interface GetWeeklySchedulesByProfessionalUseCase {
    List<WeeklySchedule> getByProfessionalId(Long professionalId);
}
