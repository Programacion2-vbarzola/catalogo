package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklyScheduleByIdUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetWeeklyScheduleByIdUseCaseImpl implements GetWeeklyScheduleByIdUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public Optional<WeeklySchedule> getWeeklyScheduleById(Long id) {
        return weeklyScheduleRepository.findOneWithEagerRelationships(id);
    }
}
