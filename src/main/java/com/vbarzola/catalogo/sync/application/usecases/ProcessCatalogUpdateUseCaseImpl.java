package com.vbarzola.catalogo.sync.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.sync.domain.model.CatalogChange;
import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.in.ProcessCatalogUpdateUseCase;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogChangesClient;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ProcessCatalogUpdateUseCaseImpl implements ProcessCatalogUpdateUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessCatalogUpdateUseCaseImpl.class);

    private final CatalogChangesClient changesClient;
    private final SyncFullCatalogUseCase syncFullCatalogUseCase;
    private final ProfessionalCategoryRepository categoryRepo;
    private final ProfessionalRepository professionalRepo;
    private final WeeklyScheduleRepository scheduleRepo;
    private final CatalogSyncStatusRepository syncStatusRepo;

    @Override
    public void process(String eventId, long newVersion) {
        CatalogSyncStatus status = syncStatusRepo.findSingleton().orElse(null);
        long localVersion = status != null && status.getLocalVersion() != null ? status.getLocalVersion() : 0L;

        // Deduplicación: ya aplicamos esta versión
        if (newVersion <= localVersion) {
            LOG.debug("Skipping duplicate: eventId={} newVersion={} already applied (localVersion={})", eventId, newVersion, localVersion);
            return;
        }

        // S3-CAT-3: detección de discontinuidad — oldestAvailable viene de Redis
        long oldestAvailable = changesClient.getOldestAvailableVersion();
        if (localVersion + 1 < oldestAvailable) {
            LOG.warn("Gap detected: localVersion={} oldestAvailable={} — triggering full sync", localVersion, oldestAvailable);
            syncFullCatalogUseCase.syncFullCatalog();
            return;
        }

        // S3-CAT-2: aplicar cada versión pendiente en orden
        for (long v = localVersion + 1; v <= newVersion; v++) {
            CatalogChange change = changesClient.fetchChanges(v);

            for (ProfessionalCategory cat : change.getCategoriesToUpsert()) {
                categoryRepo.save(cat);
            }
            for (Professional prof : change.getProfessionalsToUpsert()) {
                professionalRepo.save(prof);
            }
            for (WeeklySchedule sched : change.getSchedulesToUpsert()) {
                scheduleRepo.save(sched);
            }

            CatalogSyncStatus updated = status != null ? status : new CatalogSyncStatus();
            updated.setLocalVersion(v);
            updated.setAppliedAt(Instant.now());
            updated.setSyncType("INCREMENTAL");
            updated.setErrorMessage(null);
            syncStatusRepo.save(updated);
            status = updated;

            LOG.info(
                "Incremental version applied: v={} (cats={}, profs={}, scheds={})",
                v,
                change.getCategoriesToUpsert().size(),
                change.getProfessionalsToUpsert().size(),
                change.getSchedulesToUpsert().size()
            );
        }
    }
}
