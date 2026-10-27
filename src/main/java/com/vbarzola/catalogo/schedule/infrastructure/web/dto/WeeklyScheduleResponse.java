package com.vbarzola.catalogo.schedule.infrastructure.web.dto;

import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalResponse;
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyScheduleResponse {

    private Long id;
    private CatalogDayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer slotDurationMinutes;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private ProfessionalResponse professional;
}
