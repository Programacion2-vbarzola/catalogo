package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.ports.in.DeleteProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProfessionalCategoryUseCaseImpl implements DeleteProfessionalCategoryUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public void deleteProfessionalCategory(Long id) {
        professionalCategoryRepository.deleteById(id);
    }
}
