package com.vbarzola.catalogo.schedule.infrastructure.web.mapper;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.infrastructure.web.mapper.ProfessionalDtoMapper;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleRequest;
import com.vbarzola.catalogo.schedule.infrastructure.web.dto.WeeklyScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyScheduleDtoMapper {

    private final ProfessionalDtoMapper professionalDtoMapper;

    public WeeklySchedule toDomain(WeeklyScheduleRequest request) {
        if (request == null) {
            return null;
        }
        Professional professional = request.getProfessionalId() != null
            ? Professional.builder().id(request.getProfessionalId()).build()
            : null;
        return WeeklySchedule.builder()
            .dayOfWeek(request.getDayOfWeek())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .slotDurationMinutes(request.getSlotDurationMinutes())
            .enabled(request.getEnabled())
            .professional(professional)
            .build();
    }

    public WeeklyScheduleResponse toResponse(WeeklySchedule domain) {
        if (domain == null) {
            return null;
        }
        return WeeklyScheduleResponse.builder()
            .id(domain.getId())
            .dayOfWeek(domain.getDayOfWeek())
            .startTime(domain.getStartTime())
            .endTime(domain.getEndTime())
            .slotDurationMinutes(domain.getSlotDurationMinutes())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .professional(professionalDtoMapper.toResponse(domain.getProfessional()))
            .build();
    }
}
