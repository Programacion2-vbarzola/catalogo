package com.vbarzola.catalogo.schedule.application.usecases;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.in.PartialUpdateWeeklyScheduleUseCase;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUpdateWeeklyScheduleUseCaseImpl implements PartialUpdateWeeklyScheduleUseCase {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public Optional<WeeklySchedule> partialUpdateWeeklySchedule(WeeklySchedule weeklySchedule) {
        return weeklyScheduleRepository
            .findById(weeklySchedule.getId())
            .map(existing -> {
                if (weeklySchedule.getDayOfWeek() != null) {
                    existing.setDayOfWeek(weeklySchedule.getDayOfWeek());
                }
                if (weeklySchedule.getStartTime() != null) {
                    existing.setStartTime(weeklySchedule.getStartTime());
                }
                if (weeklySchedule.getEndTime() != null) {
                    existing.setEndTime(weeklySchedule.getEndTime());
                }
                if (weeklySchedule.getSlotDurationMinutes() != null) {
                    existing.setSlotDurationMinutes(weeklySchedule.getSlotDurationMinutes());
                }
                if (weeklySchedule.getEnabled() != null) {
                    existing.setEnabled(weeklySchedule.getEnabled());
                }
                if (weeklySchedule.getCreatedAt() != null) {
                    existing.setCreatedAt(weeklySchedule.getCreatedAt());
                }
                if (weeklySchedule.getUpdatedAt() != null) {
                    existing.setUpdatedAt(weeklySchedule.getUpdatedAt());
                }
                if (weeklySchedule.getProfessional() != null) {
                    existing.setProfessional(weeklySchedule.getProfessional());
                }
                return existing;
            })
            .map(weeklyScheduleRepository::save);
    }
}
