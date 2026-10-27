package com.vbarzola.catalogo.sync.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.sync.domain.model.CatalogSnapshot;
import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import com.vbarzola.catalogo.sync.domain.ports.out.CatedraSnapshotClient;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class SyncFullCatalogUseCaseImpl implements SyncFullCatalogUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(SyncFullCatalogUseCaseImpl.class);

    private final CatedraSnapshotClient snapshotClient;
    private final ProfessionalCategoryRepository categoryRepo;
    private final ProfessionalRepository professionalRepo;
    private final WeeklyScheduleRepository scheduleRepo;
    private final CatalogSyncStatusRepository syncStatusRepo;

    @Override
    public void syncFullCatalog() {
        LOG.info("Starting full catalog sync from catedra");

        CatalogSnapshot snapshot = snapshotClient.fetchSnapshot();

        // Delete in reverse FK order: schedules → professionals → categories
        scheduleRepo.deleteAll();
        professionalRepo.deleteAll();
        categoryRepo.deleteAll();

        // Save in FK order: categories → professionals → schedules
        for (ProfessionalCategory category : snapshot.getCategories()) {
            categoryRepo.save(category);
        }
        for (Professional professional : snapshot.getProfessionals()) {
            professionalRepo.save(professional);
        }
        for (WeeklySchedule schedule : snapshot.getSchedules()) {
            scheduleRepo.save(schedule);
        }

        // Update sync status singleton
        CatalogSyncStatus status = syncStatusRepo.findSingleton().orElseGet(CatalogSyncStatus::new);
        status.setLocalVersion(snapshot.getVersion());
        status.setAppliedAt(Instant.now());
        status.setSyncType("FULL");
        status.setErrorMessage(null);
        syncStatusRepo.save(status);

        LOG.info(
            "Full catalog sync completed: version={}, categories={}, professionals={}, schedules={}",
            snapshot.getVersion(),
            snapshot.getCategories().size(),
            snapshot.getProfessionals().size(),
            snapshot.getSchedules().size()
        );
    }
}
