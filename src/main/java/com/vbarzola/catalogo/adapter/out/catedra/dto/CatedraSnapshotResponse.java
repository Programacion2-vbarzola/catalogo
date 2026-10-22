package com.vbarzola.catalogo.adapter.out.catedra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;

// TODO: Verificar nombres exactos de campos contra la documentación de la API de cátedra
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatedraSnapshotResponse {

    private Long version;
    private List<CategoryDTO> categories;
    private List<ProfessionalDTO> professionals;
    private List<ScheduleDTO> schedules;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<ProfessionalDTO> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<ProfessionalDTO> professionals) {
        this.professionals = professionals;
    }

    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryDTO {

        private Long id;
        private String name;
        private String description;
        private Boolean enabled;
        private Instant createdAt;
        private Instant updatedAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfessionalDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private Boolean enabled;
        private Long categoryId;
        private Instant createdAt;
        private Instant updatedAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScheduleDTO {

        private Long id;
        private Long professionalId;
        private String dayOfWeek;
        private String startTime;
        private String endTime;
        private Integer slotDurationMinutes;
        private Boolean enabled;
        private Instant createdAt;
        private Instant updatedAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProfessionalId() {
            return professionalId;
        }

        public void setProfessionalId(Long professionalId) {
            this.professionalId = professionalId;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
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
    }
}
