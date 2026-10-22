package com.vbarzola.catalogo.adapter.out.persistence;

import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.domain.port.out.ProfessionalCategoryRepositoryPort;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalCategoryJpaRepository extends JpaRepository<ProfessionalCategory, Long>, ProfessionalCategoryRepositoryPort {
    @Override
    Optional<ProfessionalCategory> findById(Long id);
}
