package com.vbarzola.catalogo.professional.application.service;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalException;
import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalsUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalByIdUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.SearchProfessionalsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final GetProfessionalByIdUseCase getProfessionalByIdUseCase;
    private final GetAllProfessionalsUseCase getAllProfessionalsUseCase;
    private final SearchProfessionalsUseCase searchProfessionalsUseCase;

    public Professional getProfessionalById(Long id) {
        return getProfessionalByIdUseCase.getProfessionalById(id).orElseThrow(() -> new ProfessionalException(id));
    }

    public Page<Professional> getAllProfessionals(Pageable pageable) {
        return getAllProfessionalsUseCase.getAllProfessionals(pageable);
    }

    public Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable) {
        return getAllProfessionalsUseCase.getAllProfessionalsWithEagerRelationships(pageable);
    }

    public Page<Professional> search(String name, Long categoryId, Boolean enabled, Pageable pageable) {
        return searchProfessionalsUseCase.search(name, categoryId, enabled, pageable);
    }
}
