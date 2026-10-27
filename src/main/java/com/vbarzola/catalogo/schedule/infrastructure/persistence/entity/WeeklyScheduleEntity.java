package com.vbarzola.catalogo.schedule.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity;
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "weekly_schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyScheduleEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private CatalogDayOfWeek dayOfWeek;

    @Column(name = "start_time", length = 8, nullable = false)
    private String startTime;

    @Column(name = "end_time", length = 8, nullable = false)
    private String endTime;

    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private ProfessionalEntity professional;
}
