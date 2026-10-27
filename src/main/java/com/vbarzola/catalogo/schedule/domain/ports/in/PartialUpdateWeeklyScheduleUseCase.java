package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import java.util.Optional;

public interface PartialUpdateWeeklyScheduleUseCase {
    Optional<WeeklySchedule> partialUpdateWeeklySchedule(WeeklySchedule weeklySchedule);
}
