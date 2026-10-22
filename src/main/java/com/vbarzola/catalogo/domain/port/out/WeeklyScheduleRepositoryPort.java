package com.vbarzola.catalogo.domain.port.out;

import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WeeklyScheduleRepositoryPort {
    Page<WeeklySchedule> findAll(Pageable pageable);

    Page<WeeklySchedule> findAllWithEagerRelationships(Pageable pageable);

    Optional<WeeklySchedule> findById(Long id);

    Optional<WeeklySchedule> findOneWithEagerRelationships(Long id);

    WeeklySchedule save(WeeklySchedule entity);

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();
}
