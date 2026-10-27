package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.CreateProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProfessionalUseCaseImpl implements CreateProfessionalUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public Professional createProfessional(Professional professional) {
        return professionalRepository.save(professional);
    }
}
