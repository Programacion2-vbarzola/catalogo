package com.vbarzola.catalogo.schedule.domain.ports.in;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllWeeklySchedulesUseCase {
    Page<WeeklySchedule> getAllWeeklySchedules(Pageable pageable);

    Page<WeeklySchedule> getAllWeeklySchedulesWithEagerRelationships(Pageable pageable);
}
