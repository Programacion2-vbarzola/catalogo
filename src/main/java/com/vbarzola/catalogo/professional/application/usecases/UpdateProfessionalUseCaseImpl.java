package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.UpdateProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProfessionalUseCaseImpl implements UpdateProfessionalUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public Optional<Professional> updateProfessional(Long id, Professional professional) {
        return professionalRepository
            .findById(id)
            .map(existing -> {
                professional.setId(id);
                return professionalRepository.save(professional);
            });
    }
}
