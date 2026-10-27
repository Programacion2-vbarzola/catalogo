package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;

public interface CreateProfessionalCategoryUseCase {
    ProfessionalCategory createProfessionalCategory(ProfessionalCategory professionalCategory);
}
