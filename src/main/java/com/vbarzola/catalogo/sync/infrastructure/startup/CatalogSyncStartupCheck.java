package com.vbarzola.catalogo.sync.infrastructure.startup;

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

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        long localVersion = syncStatusRepo.findSingleton().map(s -> s.getLocalVersion() != null ? s.getLocalVersion() : 0L).orElse(0L);

        long oldestAvailable = changesClient.getOldestAvailableVersion();
        boolean neverSynced = syncStatusRepo.findSingleton().isEmpty();

        if (neverSynced || localVersion + 1 < oldestAvailable) {
            LOG.warn("Startup sync required: localVersion={} oldestAvailable={} — triggering full sync", localVersion, oldestAvailable);
            syncFullCatalogUseCase.syncFullCatalog();
        } else {
            LOG.info("Startup sync check passed: localVersion={} is up to date", localVersion);
        }
    }
}
