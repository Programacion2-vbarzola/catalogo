package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import java.util.Optional;

public interface UpdateWeeklyScheduleUseCase {
    Optional<WeeklySchedule> updateWeeklySchedule(Long id, WeeklySchedule weeklySchedule);
}
