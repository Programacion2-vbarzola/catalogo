package ar.edu.um.programacion2.catalogo.service.dto;

import ar.edu.um.programacion2.catalogo.domain.enumeration.CatalogDayOfWeek;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.programacion2.catalogo.domain.WeeklySchedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeeklyScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private CatalogDayOfWeek dayOfWeek;

    @NotNull
    @Size(max = 8)
    private String startTime;

    @NotNull
    @Size(max = 8)
    private String endTime;

    @NotNull
    @Min(value = 1)
    private Integer slotDurationMinutes;

    @NotNull
    private Boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(CatalogDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyScheduleDTO)) {
            return false;
        }

        WeeklyScheduleDTO weeklyScheduleDTO = (WeeklyScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weeklyScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyScheduleDTO{" +
            "id=" + getId() +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", slotDurationMinutes=" + getSlotDurationMinutes() +
            ", enabled='" + getEnabled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", professional=" + getProfessional() +
            "}";
    }
}
