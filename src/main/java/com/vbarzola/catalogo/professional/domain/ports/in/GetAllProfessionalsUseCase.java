package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllProfessionalsUseCase {
    Page<Professional> getAllProfessionals(Pageable pageable);

    Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable);
}
