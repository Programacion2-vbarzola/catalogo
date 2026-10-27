package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.UpdateProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProfessionalCategoryUseCaseImpl implements UpdateProfessionalCategoryUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public Optional<ProfessionalCategory> updateProfessionalCategory(Long id, ProfessionalCategory professionalCategory) {
        return professionalCategoryRepository
            .findById(id)
            .map(existing -> {
                professionalCategory.setId(id);
                return professionalCategoryRepository.save(professionalCategory);
            });
    }
}
