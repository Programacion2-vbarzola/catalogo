package com.vbarzola.catalogo.domain.model;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import java.util.List;

public class CatalogSnapshot {

    private final long version;
    private final List<ProfessionalCategory> categories;
    private final List<Professional> professionals;
    private final List<WeeklySchedule> schedules;

    public CatalogSnapshot(
        long version,
        List<ProfessionalCategory> categories,
        List<Professional> professionals,
        List<WeeklySchedule> schedules
    ) {
        this.version = version;
        this.categories = List.copyOf(categories);
        this.professionals = List.copyOf(professionals);
        this.schedules = List.copyOf(schedules);
    }

    public long getVersion() {
        return version;
    }

    public List<ProfessionalCategory> getCategories() {
        return categories;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public List<WeeklySchedule> getSchedules() {
        return schedules;
    }
}
