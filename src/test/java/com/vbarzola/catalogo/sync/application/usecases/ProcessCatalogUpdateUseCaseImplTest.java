package com.vbarzola.catalogo.sync.application.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.sync.domain.model.CatalogChange;
import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogChangesClient;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessCatalogUpdateUseCaseImplTest {

    @Mock
    private CatalogChangesClient changesClient;

    @Mock
    private SyncFullCatalogUseCase syncFullCatalogUseCase;

    @Mock
    private ProfessionalCategoryRepository categoryRepo;

    @Mock
    private ProfessionalRepository professionalRepo;

    @Mock
    private WeeklyScheduleRepository scheduleRepo;

    @Mock
    private CatalogSyncStatusRepository syncStatusRepo;

    @InjectMocks
    private ProcessCatalogUpdateUseCaseImpl useCase;

    private static CatalogChange emptyChange(long version) {
        return new CatalogChange(version, List.of(), List.of(), List.of());
    }

    // ── Deduplication ────────────────────────────────────────────────────────

    @Test
    void duplicateEvent_sameVersion_isSkipped() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(5L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));

        useCase.process("evt-1", 5L);
        useCase.process("evt-1", 5L); // mismo eventId, misma versión

        verify(changesClient, never()).fetchChanges(anyLong());
        verify(syncStatusRepo, never()).save(any());
    }

    @Test
    void duplicateEvent_olderVersion_isSkipped() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(10L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));

        useCase.process("evt-old", 7L); // newVersion < localVersion

        verify(changesClient, never()).fetchChanges(anyLong());
        verify(syncStatusRepo, never()).save(any());
    }

    @Test
    void duplicateEvent_differentId_sameVersion_isAlsoSkipped() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(3L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));

        // Kafka puede entregar el mismo evento con distinto eventId si el productor reintenta
        useCase.process("evt-A", 3L);
        useCase.process("evt-B", 3L);

        verify(changesClient, never()).fetchChanges(anyLong());
        verify(syncStatusRepo, never()).save(any());
    }

    // ── Gap detection ────────────────────────────────────────────────────────

    @Test
    void gapDetected_triggersFullSync_andSkipsIncremental() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(2L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));
        when(changesClient.getOldestAvailableVersion()).thenReturn(5L); // gap: localVersion+1=3 < oldest=5

        useCase.process("evt-new", 10L);

        verify(syncFullCatalogUseCase, times(1)).syncFullCatalog();
        verify(changesClient, never()).fetchChanges(anyLong());
    }

    @Test
    void noGap_doesNotTriggerFullSync() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(2L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));
        when(changesClient.getOldestAvailableVersion()).thenReturn(3L); // localVersion+1=3 == oldest → no gap
        when(changesClient.fetchChanges(3L)).thenReturn(emptyChange(3L));

        useCase.process("evt-3", 3L);

        verify(syncFullCatalogUseCase, never()).syncFullCatalog();
        verify(changesClient, times(1)).fetchChanges(3L);
    }

    // ── Normal incremental ───────────────────────────────────────────────────

    @Test
    void incrementalUpdate_appliesEachVersionInOrder() {
        CatalogSyncStatus status = CatalogSyncStatus.builder().localVersion(1L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(status));
        when(changesClient.getOldestAvailableVersion()).thenReturn(2L);
        when(changesClient.fetchChanges(2L)).thenReturn(emptyChange(2L));
        when(changesClient.fetchChanges(3L)).thenReturn(emptyChange(3L));

        useCase.process("evt-3", 3L); // debe aplicar v2 y v3

        verify(changesClient, times(1)).fetchChanges(2L);
        verify(changesClient, times(1)).fetchChanges(3L);
        verify(syncStatusRepo, times(2)).save(any()); // una vez por versión aplicada
    }

    @Test
    void firstSync_noExistingStatus_appliesFromVersionOne() {
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.empty()); // sin estado previo
        when(changesClient.getOldestAvailableVersion()).thenReturn(1L);
        when(changesClient.fetchChanges(1L)).thenReturn(emptyChange(1L));

        useCase.process("evt-1", 1L);

        verify(changesClient, times(1)).fetchChanges(1L);
        verify(syncStatusRepo, times(1)).save(any());
    }
}
