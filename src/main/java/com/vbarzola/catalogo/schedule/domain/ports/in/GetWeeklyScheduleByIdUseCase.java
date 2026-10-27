package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import java.util.Optional;

public interface GetWeeklyScheduleByIdUseCase {
    Optional<WeeklySchedule> getWeeklyScheduleById(Long id);
}
