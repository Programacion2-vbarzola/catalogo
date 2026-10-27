package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetAllWeeklySchedulesUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllWeeklySchedulesUseCaseImpl implements GetAllWeeklySchedulesUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public Page<WeeklySchedule> getAllWeeklySchedules(Pageable pageable) {
        return weeklyScheduleRepository.findAll(pageable);
    }

    @Override
    public Page<WeeklySchedule> getAllWeeklySchedulesWithEagerRelationships(Pageable pageable) {
        return weeklyScheduleRepository.findAllWithEagerRelationships(pageable);
    }
}
