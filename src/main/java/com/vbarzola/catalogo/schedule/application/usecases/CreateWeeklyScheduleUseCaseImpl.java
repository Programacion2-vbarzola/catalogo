package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.CreateWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateWeeklyScheduleUseCaseImpl implements CreateWeeklyScheduleUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public WeeklySchedule createWeeklySchedule(WeeklySchedule weeklySchedule) {
        return weeklyScheduleRepository.save(weeklySchedule);
    }
}
