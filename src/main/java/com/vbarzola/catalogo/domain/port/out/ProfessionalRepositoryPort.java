package com.vbarzola.catalogo.domain.port.out;

import com.vbarzola.catalogo.domain.model.Professional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessionalRepositoryPort {
    Page<Professional> findAll(Pageable pageable);

    Page<Professional> findAllWithEagerRelationships(Pageable pageable);

    Optional<Professional> findById(Long id);

    Optional<Professional> findOneWithEagerRelationships(Long id);

    Professional save(Professional entity);

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();
}
