package com.vbarzola.catalogo.sync.domain.model;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import java.util.List;

public class CatalogChange {

    private final long version;
    private final List<ProfessionalCategory> categoriesToUpsert;
    private final List<Professional> professionalsToUpsert;
    private final List<WeeklySchedule> schedulesToUpsert;

    public CatalogChange(
        long version,
        List<ProfessionalCategory> categoriesToUpsert,
        List<Professional> professionalsToUpsert,
        List<WeeklySchedule> schedulesToUpsert
    ) {
        this.version = version;
        this.categoriesToUpsert = List.copyOf(categoriesToUpsert);
        this.professionalsToUpsert = List.copyOf(professionalsToUpsert);
        this.schedulesToUpsert = List.copyOf(schedulesToUpsert);
    }

    public long getVersion() {
        return version;
    }

    public List<ProfessionalCategory> getCategoriesToUpsert() {
        return categoriesToUpsert;
    }

    public List<Professional> getProfessionalsToUpsert() {
        return professionalsToUpsert;
    }

    public List<WeeklySchedule> getSchedulesToUpsert() {
        return schedulesToUpsert;
    }
}
