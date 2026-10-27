package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.CreateProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProfessionalCategoryUseCaseImpl implements CreateProfessionalCategoryUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public ProfessionalCategory createProfessionalCategory(ProfessionalCategory professionalCategory) {
        return professionalCategoryRepository.save(professionalCategory);
    }
}
