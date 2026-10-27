package com.vbarzola.catalogo.schedule.domain.model;

import com.vbarzola.catalogo.professional.domain.model.Professional;
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
public class WeeklySchedule {

    private Long id;
    private CatalogDayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer slotDurationMinutes;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private Professional professional;
}
