package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import java.util.Optional;

public interface GetProfessionalCategoryByIdUseCase {
    Optional<ProfessionalCategory> getProfessionalCategoryById(Long id);
}
