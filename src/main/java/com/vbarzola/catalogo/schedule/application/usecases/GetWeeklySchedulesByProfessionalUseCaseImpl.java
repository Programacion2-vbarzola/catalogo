package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklySchedulesByProfessionalUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetWeeklySchedulesByProfessionalUseCaseImpl implements GetWeeklySchedulesByProfessionalUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public List<WeeklySchedule> getByProfessionalId(Long professionalId) {
        return weeklyScheduleRepository.findByProfessionalId(professionalId);
    }
}
