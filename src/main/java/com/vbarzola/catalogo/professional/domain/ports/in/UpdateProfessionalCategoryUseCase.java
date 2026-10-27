package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import java.util.Optional;

public interface UpdateProfessionalCategoryUseCase {
    Optional<ProfessionalCategory> updateProfessionalCategory(Long id, ProfessionalCategory professionalCategory);
}
