package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.ports.in.DeleteProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProfessionalUseCaseImpl implements DeleteProfessionalUseCase {

    private final ProfessionalRepository professionalRepository;

    @Override
    public void deleteProfessional(Long id) {
        professionalRepository.deleteById(id);
    }
}
