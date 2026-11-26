package com.vbarzola.catalogo.sync.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.vbarzola.catalogo.IntegrationTest;
import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalRepository;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.repository.JpaWeeklyScheduleRepository;
import com.vbarzola.catalogo.sync.domain.model.CatalogSnapshot;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import com.vbarzola.catalogo.sync.domain.ports.out.CatedraSnapshotClient;
import com.vbarzola.catalogo.sync.infrastructure.persistence.repository.JpaCatalogSyncStatusRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
class SyncFullCatalogIT {

    @MockitoBean
    private CatedraSnapshotClient snapshotClient;

    @MockitoSpyBean
    private WeeklyScheduleRepository scheduleRepo;

    @Autowired
    private SyncFullCatalogUseCase syncUseCase;

    @Autowired
    private JpaProfessionalCategoryRepository categoryJpaRepo;

    @Autowired
    private JpaProfessionalRepository professionalJpaRepo;

    @Autowired
    private JpaWeeklyScheduleRepository scheduleJpaRepo;

    @Autowired
    private JpaCatalogSyncStatusRepository syncStatusJpaRepo;

    @AfterEach
    void cleanUp() {
        scheduleJpaRepo.deleteAll();
        professionalJpaRepo.deleteAll();
        categoryJpaRepo.deleteAll();
        syncStatusJpaRepo.deleteAll();
    }

    // ── fixtures ──────────────────────────────────────────────────────────────

    private static ProfessionalCategory cat(long id, String name) {
        return ProfessionalCategory.builder().id(id).name(name).enabled(true).build();
    }

    private static Professional pro(long id, String first, String last, ProfessionalCategory category) {
        return Professional.builder().id(id).firstName(first).lastName(last).enabled(true).category(category).build();
    }

    private static WeeklySchedule sched(long id, Professional professional) {
        return WeeklySchedule.builder()
            .id(id)
            .dayOfWeek(CatalogDayOfWeek.MONDAY)
            .startTime("08:00")
            .endTime("12:00")
            .slotDurationMinutes(30)
            .enabled(true)
            .professional(professional)
            .build();
    }

    // ── Guardado completo en BD ───────────────────────────────────────────────

    @Test
    void syncFullCatalog_persistsCategories_toDatabase() {
        ProfessionalCategory cat1 = cat(1L, "Cardiology");
        ProfessionalCategory cat2 = cat(2L, "Neurology");
        givenSnapshot(1L, List.of(cat1, cat2), List.of(), List.of());

        syncUseCase.syncFullCatalog();

        assertThat(categoryJpaRepo.count()).isEqualTo(2);
        assertThat(categoryJpaRepo.findAll()).extracting("name").containsExactlyInAnyOrder("Cardiology", "Neurology");
    }

    @Test
    void syncFullCatalog_persistsProfessionals_toDatabase() {
        ProfessionalCategory cat = cat(1L, "Cardiology");
        Professional pro = pro(10L, "Ana", "Gomez", cat);
        givenSnapshot(1L, List.of(cat), List.of(pro), List.of());

        syncUseCase.syncFullCatalog();

        assertThat(professionalJpaRepo.count()).isEqualTo(1);
        assertThat(professionalJpaRepo.findAll()).extracting("firstName").containsExactly("Ana");
    }

    @Test
    void syncFullCatalog_persistsSchedules_toDatabase() {
        ProfessionalCategory cat = cat(1L, "Cardiology");
        Professional pro = pro(10L, "Ana", "Gomez", cat);
        WeeklySchedule sched = sched(100L, pro);
        givenSnapshot(1L, List.of(cat), List.of(pro), List.of(sched));

        syncUseCase.syncFullCatalog();

        assertThat(scheduleJpaRepo.count()).isEqualTo(1);
    }

    @Test
    void syncFullCatalog_updatesSyncStatus_withSnapshotVersion() {
        givenSnapshot(7L, List.of(), List.of(), List.of());

        syncUseCase.syncFullCatalog();

        assertThat(syncStatusJpaRepo.findSingleton())
            .isPresent()
            .hasValueSatisfying(s -> {
                assertThat(s.getLocalVersion()).isEqualTo(7L);
                assertThat(s.getSyncType()).isEqualTo("FULL");
                assertThat(s.getAppliedAt()).isNotNull();
                assertThat(s.getErrorMessage()).isNull();
            });
    }

    // ── Reemplazo completo (no acumulación) ───────────────────────────────────

    @Test
    void syncFullCatalog_replacesPreviousData_notAccumulates() {
        // Primera sincronización
        ProfessionalCategory cat1 = cat(1L, "FirstSync");
        givenSnapshot(1L, List.of(cat1), List.of(), List.of());
        syncUseCase.syncFullCatalog();
        assertThat(categoryJpaRepo.count()).isEqualTo(1);

        // Segunda sincronización con datos distintos
        ProfessionalCategory cat2 = cat(2L, "SecondSync");
        ProfessionalCategory cat3 = cat(3L, "AlsoSecond");
        givenSnapshot(2L, List.of(cat2, cat3), List.of(), List.of());
        syncUseCase.syncFullCatalog();

        assertThat(categoryJpaRepo.count()).isEqualTo(2);
        assertThat(categoryJpaRepo.findAll()).extracting("name").containsExactlyInAnyOrder("SecondSync", "AlsoSecond");
    }

    // ── Atomicidad: rollback si falla un save ─────────────────────────────────

    @Test
    void syncFullCatalog_isAtomic_rollsBackOnScheduleSaveFailure() {
        // Población inicial que debe sobrevivir al rollback
        ProfessionalCategory existingCat = cat(99L, "ExistingCategory");
        givenSnapshot(1L, List.of(existingCat), List.of(), List.of());
        syncUseCase.syncFullCatalog();
        assertThat(categoryJpaRepo.count()).isEqualTo(1);

        // Segunda sincronización que falla al guardar el primer horario
        ProfessionalCategory cat = cat(1L, "NewCategory");
        Professional pro = pro(10L, "Ana", "Gomez", cat);
        WeeklySchedule sched = sched(100L, pro);
        givenSnapshot(2L, List.of(cat), List.of(pro), List.of(sched));
        doThrow(new RuntimeException("DB failure")).when(scheduleRepo).save(any());

        assertThatThrownBy(() -> syncUseCase.syncFullCatalog()).isInstanceOf(RuntimeException.class);

        // La BD debe quedar en el estado anterior: la categoría original, no la nueva
        assertThat(categoryJpaRepo.count()).isEqualTo(1);
        assertThat(categoryJpaRepo.findAll()).extracting("name").containsExactly("ExistingCategory");
        assertThat(professionalJpaRepo.count()).isZero();
        assertThat(scheduleJpaRepo.count()).isZero();
        // El sync status no debe haberse actualizado a la versión 2
        assertThat(syncStatusJpaRepo.findSingleton()).hasValueSatisfying(s -> assertThat(s.getLocalVersion()).isEqualTo(1L));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void givenSnapshot(long version, List<ProfessionalCategory> cats, List<Professional> pros, List<WeeklySchedule> scheds) {
        org.mockito.Mockito.when(snapshotClient.fetchSnapshot()).thenReturn(new CatalogSnapshot(version, cats, pros, scheds));
    }
}
