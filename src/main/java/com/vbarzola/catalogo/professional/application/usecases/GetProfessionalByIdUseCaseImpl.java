package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalByIdUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProfessionalByIdUseCaseImpl implements GetProfessionalByIdUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public Optional<Professional> getProfessionalById(Long id) {
        return professionalRepository.findOneWithEagerRelationships(id);
    }
}
