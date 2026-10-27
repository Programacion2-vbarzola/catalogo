package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalsUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllProfessionalsUseCaseImpl implements GetAllProfessionalsUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public Page<Professional> getAllProfessionals(Pageable pageable) {
        return professionalRepository.findAll(pageable);
    }

    @Override
    public Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable) {
        return professionalRepository.findAllWithEagerRelationships(pageable);
    }
}
