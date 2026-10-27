package com.vbarzola.catalogo.schedule.application.service;

import com.vbarzola.catalogo.schedule.application.exception.WeeklyScheduleException;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.CreateWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.DeleteWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetAllWeeklySchedulesUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklyScheduleByIdUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.GetWeeklySchedulesByProfessionalUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.PartialUpdateWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.in.UpdateWeeklyScheduleUseCase;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {

    private final CreateWeeklyScheduleUseCase createWeeklyScheduleUseCase;
    private final UpdateWeeklyScheduleUseCase updateWeeklyScheduleUseCase;
    private final PartialUpdateWeeklyScheduleUseCase partialUpdateWeeklyScheduleUseCase;
    private final GetWeeklyScheduleByIdUseCase getWeeklyScheduleByIdUseCase;
    private final GetAllWeeklySchedulesUseCase getAllWeeklySchedulesUseCase;
    private final GetWeeklySchedulesByProfessionalUseCase getWeeklySchedulesByProfessionalUseCase;
    private final DeleteWeeklyScheduleUseCase deleteWeeklyScheduleUseCase;

    public WeeklySchedule createWeeklySchedule(WeeklySchedule weeklySchedule) {
        return createWeeklyScheduleUseCase.createWeeklySchedule(weeklySchedule);
    }

    public WeeklySchedule getWeeklyScheduleById(Long id) {
        return getWeeklyScheduleByIdUseCase.getWeeklyScheduleById(id).orElseThrow(() -> new WeeklyScheduleException(id));
    }

    public Optional<WeeklySchedule> updateWeeklySchedule(Long id, WeeklySchedule weeklySchedule) {
        return updateWeeklyScheduleUseCase.updateWeeklySchedule(id, weeklySchedule);
    }

    public Optional<WeeklySchedule> partialUpdateWeeklySchedule(WeeklySchedule weeklySchedule) {
        return partialUpdateWeeklyScheduleUseCase.partialUpdateWeeklySchedule(weeklySchedule);
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

    public void deleteWeeklySchedule(Long id) {
        deleteWeeklyScheduleUseCase.deleteWeeklySchedule(id);
    }
}
