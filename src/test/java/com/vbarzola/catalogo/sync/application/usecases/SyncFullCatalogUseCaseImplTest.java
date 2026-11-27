package com.vbarzola.catalogo.sync.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.sync.domain.model.CatalogSnapshot;
import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import com.vbarzola.catalogo.sync.domain.ports.out.CatedraSnapshotClient;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SyncFullCatalogUseCaseImplTest {

    @Mock
    private CatedraSnapshotClient snapshotClient;

    @Mock
    private ProfessionalCategoryRepository categoryRepo;

    @Mock
    private ProfessionalRepository professionalRepo;

    @Mock
    private WeeklyScheduleRepository scheduleRepo;

    @Mock
    private CatalogSyncStatusRepository syncStatusRepo;

    @InjectMocks
    private SyncFullCatalogUseCaseImpl useCase;

    private ProfessionalCategory cat1;
    private ProfessionalCategory cat2;
    private Professional pro1;
    private Professional pro2;
    private WeeklySchedule sched1;
    private WeeklySchedule sched2;

    @BeforeEach
    void setUp() {
        cat1 = ProfessionalCategory.builder().id(1L).name("Cardiology").enabled(true).build();
        cat2 = ProfessionalCategory.builder().id(2L).name("Neurology").enabled(true).build();

        pro1 = Professional.builder().id(10L).firstName("Ana").lastName("Gomez").enabled(true).category(cat1).build();
        pro2 = Professional.builder().id(11L).firstName("Luis").lastName("Paz").enabled(true).category(cat2).build();

        sched1 = WeeklySchedule.builder()
            .id(100L)
            .dayOfWeek(CatalogDayOfWeek.MONDAY)
            .startTime("08:00")
            .endTime("12:00")
            .slotDurationMinutes(30)
            .enabled(true)
            .professional(pro1)
            .build();
        sched2 = WeeklySchedule.builder()
            .id(101L)
            .dayOfWeek(CatalogDayOfWeek.TUESDAY)
            .startTime("09:00")
            .endTime("13:00")
            .slotDurationMinutes(30)
            .enabled(true)
            .professional(pro2)
            .build();

        when(categoryRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(professionalRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(scheduleRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.empty());
        when(syncStatusRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    // ── Guardado completo de las tres colecciones ─────────────────────────────

    @Test
    void syncFullCatalog_savesAllCategoriesFromSnapshot() {
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(1L, Instant.now(), List.of(cat1, cat2), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(categoryRepo, times(1)).save(cat1);
        verify(categoryRepo, times(1)).save(cat2);
    }

    @Test
    void syncFullCatalog_savesAllProfessionalsFromSnapshot() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1, cat2), List.of(pro1, pro2), List.of())
        );

        useCase.syncFullCatalog();

        verify(professionalRepo, times(1)).save(pro1);
        verify(professionalRepo, times(1)).save(pro2);
    }

    @Test
    void syncFullCatalog_savesAllSchedulesFromSnapshot() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1, cat2), List.of(pro1, pro2), List.of(sched1, sched2))
        );

        useCase.syncFullCatalog();

        verify(scheduleRepo, times(1)).save(sched1);
        verify(scheduleRepo, times(1)).save(sched2);
    }

    // ── Orden de operaciones ──────────────────────────────────────────────────

    @Test
    void syncFullCatalog_deletesInReverseFkOrder_beforeSaving() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1), List.of(pro1), List.of(sched1))
        );

        useCase.syncFullCatalog();

        InOrder order = inOrder(scheduleRepo, professionalRepo, categoryRepo);
        order.verify(scheduleRepo).deleteAll();
        order.verify(professionalRepo).deleteAll();
        order.verify(categoryRepo).deleteAll();
    }

    @Test
    void syncFullCatalog_savesInFkOrder_afterDeletes() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1), List.of(pro1), List.of(sched1))
        );

        useCase.syncFullCatalog();

        InOrder order = inOrder(categoryRepo, professionalRepo, scheduleRepo);
        order.verify(categoryRepo).save(cat1);
        order.verify(professionalRepo).save(pro1);
        order.verify(scheduleRepo).save(sched1);
    }

    @Test
    void syncFullCatalog_deletesBeforeSaves() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1), List.of(pro1), List.of(sched1))
        );

        useCase.syncFullCatalog();

        InOrder order = inOrder(scheduleRepo, professionalRepo, categoryRepo);
        order.verify(scheduleRepo).deleteAll();
        order.verify(professionalRepo).deleteAll();
        order.verify(categoryRepo).deleteAll();
        order.verify(categoryRepo).save(any());
    }

    // ── Sync status ───────────────────────────────────────────────────────────

    @Test
    void syncFullCatalog_updatesSyncStatus_withSnapshotVersion() {
        long snapshotVersion = 42L;
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(snapshotVersion, Instant.now(), List.of(), List.of(), List.of())
        );

        useCase.syncFullCatalog();

        verify(syncStatusRepo).save(argThat(status -> status.getLocalVersion() == snapshotVersion));
    }

    @Test
    void syncFullCatalog_updatesSyncStatus_afterAllCollectionsSaved() {
        when(snapshotClient.fetchSnapshot()).thenReturn(
            new CatalogSnapshot(1L, Instant.now(), List.of(cat1), List.of(pro1), List.of(sched1))
        );

        useCase.syncFullCatalog();

        InOrder order = inOrder(scheduleRepo, syncStatusRepo);
        order.verify(scheduleRepo).save(any());
        order.verify(syncStatusRepo).save(any());
    }

    @Test
    void syncFullCatalog_setsFullSyncType_onStatus() {
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(1L, Instant.now(), List.of(), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(syncStatusRepo).save(argThat(status -> "FULL".equals(status.getSyncType())));
    }

    @Test
    void syncFullCatalog_clearsErrorMessage_onStatus() {
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(1L, Instant.now(), List.of(), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(syncStatusRepo).save(argThat(status -> status.getErrorMessage() == null));
    }

    @Test
    void syncFullCatalog_reusesSingleton_whenStatusAlreadyExists() {
        CatalogSyncStatus existing = CatalogSyncStatus.builder().id(1L).localVersion(0L).build();
        when(syncStatusRepo.findSingleton()).thenReturn(Optional.of(existing));
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(5L, Instant.now(), List.of(), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(syncStatusRepo, never()).save(argThat(status -> status.getId() == null));
        verify(syncStatusRepo).save(argThat(status -> Long.valueOf(1L).equals(status.getId()) && status.getLocalVersion() == 5L));
    }

    // ── Snapshot vacío ────────────────────────────────────────────────────────

    @Test
    void syncFullCatalog_emptySnapshot_clearsDbAndUpdatesStatus() {
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(0L, Instant.now(), List.of(), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(scheduleRepo).deleteAll();
        verify(professionalRepo).deleteAll();
        verify(categoryRepo).deleteAll();
        verify(categoryRepo, never()).save(any());
        verify(professionalRepo, never()).save(any());
        verify(scheduleRepo, never()).save(any());
        verify(syncStatusRepo).save(any());
    }

    // ── Status: appliedAt se fija ─────────────────────────────────────────────

    @Test
    void syncFullCatalog_setsAppliedAt_onStatus() {
        when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(1L, Instant.now(), List.of(), List.of(), List.of()));

        useCase.syncFullCatalog();

        verify(syncStatusRepo).save(argThat(status -> status.getAppliedAt() != null));
    }
}
