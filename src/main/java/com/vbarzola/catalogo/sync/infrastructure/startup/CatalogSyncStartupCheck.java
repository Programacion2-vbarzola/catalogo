package com.vbarzola.catalogo.sync.infrastructure.startup;

import com.vbarzola.catalogo.config.ApplicationProperties;
import com.vbarzola.catalogo.sync.domain.ports.in.ProcessCatalogUpdateUseCase;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogChangesClient;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CatalogSyncStartupCheck {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogSyncStartupCheck.class);

    private final CatalogSyncStatusRepository syncStatusRepo;
    private final CatalogChangesClient changesClient;
    private final SyncFullCatalogUseCase syncFullCatalogUseCase;
    private final ProcessCatalogUpdateUseCase processCatalogUpdateUseCase;
    private final ApplicationProperties props;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        var status = syncStatusRepo.findSingleton();
        long localVersion = status.map(s -> s.getLocalVersion() != null ? s.getLocalVersion() : 0L).orElse(0L);
        boolean neverSynced = status.isEmpty();

        // Step 1: Kafka deduplication check — skip all sync if already at latest version
        long latestAvailable = changesClient.getLatestAvailableVersion();
        if (!neverSynced && localVersion >= latestAvailable) {
            LOG.info("Startup deduplication check: localVersion={} >= latestAvailable={} — no sync needed", localVersion, latestAvailable);
            return;
        }

        // Step 2: Determine full sync vs incremental
        long oldestAvailable = changesClient.getOldestAvailableVersion();
        if (neverSynced || localVersion < oldestAvailable) {
            LOG.warn("Startup sync required: localVersion={} oldestAvailable={} — triggering full sync", localVersion, oldestAvailable);
            syncFullCatalogUseCase.syncFullCatalog();
        } else {
            long versionsToApply = latestAvailable - localVersion;
            int threshold = props.getCatedra().getIncrementalVersionThreshold();
            if (versionsToApply > threshold) {
                LOG.warn(
                    "Startup threshold exceeded: {} versions to apply > threshold={} — triggering full sync",
                    versionsToApply,
                    threshold
                );
                syncFullCatalogUseCase.syncFullCatalog();
            } else {
                LOG.info(
                    "Startup incremental catchup: applying versions {}..{} (oldest={})",
                    localVersion + 1,
                    latestAvailable,
                    oldestAvailable
                );
                processCatalogUpdateUseCase.process("startup-catchup", latestAvailable);
            }
        }
    }
}
