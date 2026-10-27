package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import java.util.List;

public interface GetAllProfessionalCategoriesUseCase {
    List<ProfessionalCategory> getAllProfessionalCategories();
}
