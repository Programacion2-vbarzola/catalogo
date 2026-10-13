package com.vbarzola.catalogo.domain.port.in;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WeeklyScheduleUseCase {
    WeeklyScheduleDTO save(WeeklyScheduleDTO dto);

    WeeklyScheduleDTO update(WeeklyScheduleDTO dto);

    Optional<WeeklyScheduleDTO> partialUpdate(WeeklyScheduleDTO dto);

    Page<WeeklyScheduleDTO> findAll(Pageable pageable);

    Page<WeeklyScheduleDTO> findAllWithEagerRelationships(Pageable pageable);

    Optional<WeeklyScheduleDTO> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
