package com.vbarzola.catalogo.domain.port.out;

import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import java.util.List;
import java.util.Optional;

public interface ProfessionalCategoryRepositoryPort {
    List<ProfessionalCategory> findAll();

    Optional<ProfessionalCategory> findById(Long id);

    ProfessionalCategory save(ProfessionalCategory entity);

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();
}
