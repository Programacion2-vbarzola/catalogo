package com.vbarzola.catalogo.domain.port.in;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchProfessionalsUseCase {
    Page<ProfessionalDTO> search(String name, Long categoryId, Boolean enabled, Pageable pageable);
}
