package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import java.util.Optional;

public interface UpdateProfessionalUseCase {
    Optional<Professional> updateProfessional(Long id, Professional professional);
}
