package com.vbarzola.catalogo.domain.port.in;

import com.vbarzola.catalogo.domain.dto.ProfessionalCategoryDTO;
import java.util.List;
import java.util.Optional;

public interface ProfessionalCategoryUseCase {
    ProfessionalCategoryDTO save(ProfessionalCategoryDTO dto);

    ProfessionalCategoryDTO update(ProfessionalCategoryDTO dto);

    Optional<ProfessionalCategoryDTO> partialUpdate(ProfessionalCategoryDTO dto);

    List<ProfessionalCategoryDTO> findAll();

    Optional<ProfessionalCategoryDTO> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
