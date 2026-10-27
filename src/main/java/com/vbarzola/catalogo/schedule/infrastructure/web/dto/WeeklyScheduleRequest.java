package com.vbarzola.catalogo.schedule.infrastructure.web.dto;

import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class WeeklyScheduleRequest {

    @NotNull
    private CatalogDayOfWeek dayOfWeek;

    @NotNull
    @Size(max = 8)
    private String startTime;

    @NotNull
    @Size(max = 8)
    private String endTime;

    @NotNull
    @Min(1)
    private Integer slotDurationMinutes;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Long professionalId;
}
