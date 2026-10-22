package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.model.CatalogSnapshot;
import com.vbarzola.catalogo.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.domain.port.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.domain.port.out.CatalogSyncStatusRepositoryPort;
import com.vbarzola.catalogo.domain.port.out.CatedraSnapshotClient;
import com.vbarzola.catalogo.domain.port.out.ProfessionalCategoryRepositoryPort;
import com.vbarzola.catalogo.domain.port.out.ProfessionalRepositoryPort;
import com.vbarzola.catalogo.domain.port.out.WeeklyScheduleRepositoryPort;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SyncFullCatalogService implements SyncFullCatalogUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(SyncFullCatalogService.class);

    private final CatedraSnapshotClient snapshotClient;
    private final ProfessionalCategoryRepositoryPort categoryRepo;
    private final ProfessionalRepositoryPort professionalRepo;
    private final WeeklyScheduleRepositoryPort scheduleRepo;
    private final CatalogSyncStatusRepositoryPort syncStatusRepo;

    public SyncFullCatalogService(
        CatedraSnapshotClient snapshotClient,
        ProfessionalCategoryRepositoryPort categoryRepo,
        ProfessionalRepositoryPort professionalRepo,
        WeeklyScheduleRepositoryPort scheduleRepo,
        CatalogSyncStatusRepositoryPort syncStatusRepo
    ) {
        this.snapshotClient = snapshotClient;
        this.categoryRepo = categoryRepo;
        this.professionalRepo = professionalRepo;
        this.scheduleRepo = scheduleRepo;
        this.syncStatusRepo = syncStatusRepo;
    }

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
