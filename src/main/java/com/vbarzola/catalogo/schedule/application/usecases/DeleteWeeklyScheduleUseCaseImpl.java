package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.ports.in.DeleteWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteWeeklyScheduleUseCaseImpl implements DeleteWeeklyScheduleUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public void deleteWeeklySchedule(Long id) {
        weeklyScheduleRepository.deleteById(id);
    }
}
