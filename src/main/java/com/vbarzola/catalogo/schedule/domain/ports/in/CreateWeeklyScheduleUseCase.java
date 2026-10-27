package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;

public interface CreateWeeklyScheduleUseCase {
    WeeklySchedule createWeeklySchedule(WeeklySchedule weeklySchedule);
}
