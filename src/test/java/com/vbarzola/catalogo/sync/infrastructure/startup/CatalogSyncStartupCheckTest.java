package com.vbarzola.catalogo.sync.infrastructure.startup;

import static org.mockito.Mockito.*;

import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogChangesClient;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CatalogSyncStartupCheckTest {

    @Mock
    private CatalogSyncStatusRepository syncStatusRepo;

    @Mock
    private CatalogChangesClient changesClient;

    @Mock
    private SyncFullCatalogUseCase syncFullCatalogUseCase;

    @InjectMocks
    private CatalogSyncStartupCheck startupCheck;

    @BeforeEach
    void setUp() {
        // Default: no sync needed from dedup check and no gap
        lenient().when(changesClient.getLatestAvailableVersion()).thenReturn(5L);
        lenient().when(changesClient.getOldestAvailableVersion()).thenReturn(1L);
    }

    // ── Step 1: Deduplication check ───────────────────────────────────────────

    @Test
    void onStartup_skipsSyncCompletely_whenLocalVersionEqualsLatest() {
        givenSyncedAt(5L);
        when(changesClient.getLatestAvailableVersion()).thenReturn(5L);

        startupCheck.onStartup();

        verify(syncFullCatalogUseCase, never()).syncFullCatalog();
        verify(changesClient, never()).getOldestAvailableVersion();
    }

    @Test
    void onStartup_skipsSyncCompletely_whenLocalVersionAheadOfLatest() {
        givenSyncedAt(7L);
        when(changesClient.getLatestAvailableVersion()).thenReturn(5L);

        startupCheck.onStartup();

        verify(syncFullCatalogUseCase, never()).syncFullCatalog();
        verify(changesClient, never()).getOldestAvailableVersion();
    }

    // ── Step 2a: Full sync — never synced ─────────────────────────────────────

    @Test
    void onStartup_triggersFullSync_whenNeverSynced() {
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.empty());
        when(changesClient.getLatestAvailableVersion()).thenReturn(5L);
        when(changesClient.getOldestAvailableVersion()).thenReturn(1L);

        startupCheck.onStartup();

        verify(syncFullCatalogUseCase).syncFullCatalog();
    }

    // ── Step 2b: Full sync — gap detected ─────────────────────────────────────

    @Test
    void onStartup_triggersFullSync_whenGapDetected() {
        givenSyncedAt(2L);
        when(changesClient.getLatestAvailableVersion()).thenReturn(10L);
        when(changesClient.getOldestAvailableVersion()).thenReturn(5L); // localVersion+1=3 < 5 → gap

        startupCheck.onStartup();

        verify(syncFullCatalogUseCase).syncFullCatalog();
    }

    // ── Step 2c: Incremental — within range, Kafka consumer handles it ────────

    @Test
    void onStartup_doesNotTriggerFullSync_whenIncrementalRangeCoversGap() {
        givenSyncedAt(3L);
        when(changesClient.getLatestAvailableVersion()).thenReturn(10L);
        when(changesClient.getOldestAvailableVersion()).thenReturn(4L); // localVersion+1=4 >= 4 → no gap

        startupCheck.onStartup();

        verify(syncFullCatalogUseCase, never()).syncFullCatalog();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void givenSyncedAt(long version) {
        CatalogSyncStatus status = new CatalogSyncStatus();
        status.setLocalVersion(version);
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));
    }
}
