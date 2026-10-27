package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalCategoriesUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllProfessionalCategoriesUseCaseImpl implements GetAllProfessionalCategoriesUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public List<ProfessionalCategory> getAllProfessionalCategories() {
        return professionalCategoryRepository.findAll();
    }
}
