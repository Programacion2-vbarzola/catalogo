package com.vbarzola.catalogo.sync.infrastructure.redis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogChangeRedisDto {

    private Long version;
    private Changes changes;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Changes getChanges() {
        return changes;
    }

    public void setChanges(Changes changes) {
        this.changes = changes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Changes {

        private List<Long> professionalCategories;
        private List<Long> professionals;
        private List<Long> weeklySchedules;

        public List<Long> getProfessionalCategories() {
            return professionalCategories;
        }

        public void setProfessionalCategories(List<Long> v) {
            this.professionalCategories = v;
        }

        public List<Long> getProfessionals() {
            return professionals;
        }

        public void setProfessionals(List<Long> v) {
            this.professionals = v;
        }

        public List<Long> getWeeklySchedules() {
            return weeklySchedules;
        }

        public void setWeeklySchedules(List<Long> v) {
            this.weeklySchedules = v;
        }
    }
}
