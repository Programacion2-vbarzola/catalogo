package com.vbarzola.catalogo.domain.port.in;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessionalUseCase {
    ProfessionalDTO save(ProfessionalDTO dto);

    ProfessionalDTO update(ProfessionalDTO dto);

    Optional<ProfessionalDTO> partialUpdate(ProfessionalDTO dto);

    Page<ProfessionalDTO> findAll(Pageable pageable);

    Page<ProfessionalDTO> findAllWithEagerRelationships(Pageable pageable);

    Optional<ProfessionalDTO> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
