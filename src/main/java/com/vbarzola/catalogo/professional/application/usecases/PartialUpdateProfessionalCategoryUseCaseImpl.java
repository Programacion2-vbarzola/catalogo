package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.PartialUpdateProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUpdateProfessionalCategoryUseCaseImpl implements PartialUpdateProfessionalCategoryUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public Optional<ProfessionalCategory> partialUpdateProfessionalCategory(ProfessionalCategory professionalCategory) {
        return professionalCategoryRepository
            .findById(professionalCategory.getId())
            .map(existing -> {
                if (professionalCategory.getName() != null) {
                    existing.setName(professionalCategory.getName());
                }
                if (professionalCategory.getDescription() != null) {
                    existing.setDescription(professionalCategory.getDescription());
                }
                if (professionalCategory.getEnabled() != null) {
                    existing.setEnabled(professionalCategory.getEnabled());
                }
                if (professionalCategory.getCreatedAt() != null) {
                    existing.setCreatedAt(professionalCategory.getCreatedAt());
                }
                if (professionalCategory.getUpdatedAt() != null) {
                    existing.setUpdatedAt(professionalCategory.getUpdatedAt());
                }
                return existing;
            })
            .map(professionalCategoryRepository::save);
    }
}
