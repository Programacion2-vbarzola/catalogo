package com.vbarzola.catalogo.professional.application.usecases;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalCategoryByIdUseCase;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProfessionalCategoryByIdUseCaseImpl implements GetProfessionalCategoryByIdUseCase {

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    @Override
    public Optional<ProfessionalCategory> getProfessionalCategoryById(Long id) {
        return professionalCategoryRepository.findById(id);
    }
}
