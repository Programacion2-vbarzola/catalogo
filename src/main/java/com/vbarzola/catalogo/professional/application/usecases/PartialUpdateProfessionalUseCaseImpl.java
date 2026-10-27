package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.PartialUpdateProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUpdateProfessionalUseCaseImpl implements PartialUpdateProfessionalUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public Optional<Professional> partialUpdateProfessional(Professional professional) {
        return professionalRepository
            .findById(professional.getId())
            .map(existing -> {
                if (professional.getFirstName() != null) {
                    existing.setFirstName(professional.getFirstName());
                }
                if (professional.getLastName() != null) {
                    existing.setLastName(professional.getLastName());
                }
                if (professional.getEnabled() != null) {
                    existing.setEnabled(professional.getEnabled());
                }
                if (professional.getCreatedAt() != null) {
                    existing.setCreatedAt(professional.getCreatedAt());
                }
                if (professional.getUpdatedAt() != null) {
                    existing.setUpdatedAt(professional.getUpdatedAt());
                }
                if (professional.getCategory() != null) {
                    existing.setCategory(professional.getCategory());
                }
                return existing;
            })
            .map(professionalRepository::save);
    }
}
