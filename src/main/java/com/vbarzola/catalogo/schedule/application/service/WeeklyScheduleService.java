package com.vbarzola.catalogo.schedule.application.service;

import com.vbarzola.catalogo.schedule.application.exception.WeeklyScheduleException;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetAllWeeklySchedulesUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklyScheduleByIdUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklySchedulesByProfessionalUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {

    private final GetWeeklyScheduleByIdUseCase getWeeklyScheduleByIdUseCase;
    private final GetAllWeeklySchedulesUseCase getAllWeeklySchedulesUseCase;
    private final GetWeeklySchedulesByProfessionalUseCase getWeeklySchedulesByProfessionalUseCase;

    public WeeklySchedule getWeeklyScheduleById(Long id) {
        return getWeeklyScheduleByIdUseCase.getWeeklyScheduleById(id).orElseThrow(() -> new WeeklyScheduleException(id));
    }

    public Page<WeeklySchedule> getAllWeeklySchedules(Pageable pageable) {
        return getAllWeeklySchedulesUseCase.getAllWeeklySchedules(pageable);
    }

    public Page<WeeklySchedule> getAllWeeklySchedulesWithEagerRelationships(Pageable pageable) {
        return getAllWeeklySchedulesUseCase.getAllWeeklySchedulesWithEagerRelationships(pageable);
    }

    public List<WeeklySchedule> getByProfessionalId(Long professionalId) {
        return getWeeklySchedulesByProfessionalUseCase.getByProfessionalId(professionalId);
    }
}
