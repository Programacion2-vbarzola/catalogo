package com.vbarzola.catalogo.professional.application.service;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalException;
import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.in.CreateProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.DeleteProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalsUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalByIdUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.PartialUpdateProfessionalUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.SearchProfessionalsUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.UpdateProfessionalUseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final CreateProfessionalUseCase createProfessionalUseCase;
    private final UpdateProfessionalUseCase updateProfessionalUseCase;
    private final PartialUpdateProfessionalUseCase partialUpdateProfessionalUseCase;
    private final GetProfessionalByIdUseCase getProfessionalByIdUseCase;
    private final GetAllProfessionalsUseCase getAllProfessionalsUseCase;
    private final DeleteProfessionalUseCase deleteProfessionalUseCase;
    private final SearchProfessionalsUseCase searchProfessionalsUseCase;

    public Professional createProfessional(Professional professional) {
        return createProfessionalUseCase.createProfessional(professional);
    }

    public Professional getProfessionalById(Long id) {
        return getProfessionalByIdUseCase.getProfessionalById(id).orElseThrow(() -> new ProfessionalException(id));
    }

    public Optional<Professional> updateProfessional(Long id, Professional professional) {
        return updateProfessionalUseCase.updateProfessional(id, professional);
    }

    public Optional<Professional> partialUpdateProfessional(Professional professional) {
        return partialUpdateProfessionalUseCase.partialUpdateProfessional(professional);
    }

    public Page<Professional> getAllProfessionals(Pageable pageable) {
        return getAllProfessionalsUseCase.getAllProfessionals(pageable);
    }

    public Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable) {
        return getAllProfessionalsUseCase.getAllProfessionalsWithEagerRelationships(pageable);
    }

    public void deleteProfessional(Long id) {
        deleteProfessionalUseCase.deleteProfessional(id);
    }

    public Page<Professional> search(String name, Long categoryId, Boolean enabled, Pageable pageable) {
        return searchProfessionalsUseCase.search(name, categoryId, enabled, pageable);
    }
}
