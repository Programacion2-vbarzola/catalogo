package com.vbarzola.catalogo.professional.domain.ports.in;

import com.vbarzola.catalogo.professional.domain.model.Professional;

public interface CreateProfessionalUseCase {
    Professional createProfessional(Professional professional);
}
