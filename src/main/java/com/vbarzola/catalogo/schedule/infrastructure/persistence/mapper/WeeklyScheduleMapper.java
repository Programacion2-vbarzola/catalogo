package com.vbarzola.catalogo.schedule.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.professional.infrastructure.persistence.mapper.ProfessionalMapper;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.entity.WeeklyScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyScheduleMapper {

    private final ProfessionalMapper professionalMapper;

    public WeeklyScheduleEntity toEntity(WeeklySchedule domain) {
        if (domain == null) {
            return null;
        }
        return WeeklyScheduleEntity.builder()
            .id(domain.getId())
            .dayOfWeek(domain.getDayOfWeek())
            .startTime(domain.getStartTime())
            .endTime(domain.getEndTime())
            .slotDurationMinutes(domain.getSlotDurationMinutes())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .professional(professionalMapper.toEntity(domain.getProfessional()))
            .build();
    }

    public WeeklySchedule toDomainModel(WeeklyScheduleEntity entity) {
        if (entity == null) {
            return null;
        }
        return WeeklySchedule.builder()
            .id(entity.getId())
            .dayOfWeek(entity.getDayOfWeek())
            .startTime(entity.getStartTime())
            .endTime(entity.getEndTime())
            .slotDurationMinutes(entity.getSlotDurationMinutes())
            .enabled(entity.getEnabled())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .professional(professionalMapper.toDomainModel(entity.getProfessional()))
            .build();
    }
}
