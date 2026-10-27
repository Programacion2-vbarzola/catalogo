package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.UpdateWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateWeeklyScheduleUseCaseImpl implements UpdateWeeklyScheduleUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public Optional<WeeklySchedule> updateWeeklySchedule(Long id, WeeklySchedule weeklySchedule) {
        return weeklyScheduleRepository
            .findById(id)
            .map(existing -> {
                weeklySchedule.setId(id);
                return weeklyScheduleRepository.save(weeklySchedule);
            });
    }
}
