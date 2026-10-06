package ar.edu.um.programacion2.catalogo.repository;

import ar.edu.um.programacion2.catalogo.domain.ProfessionalCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalCategoryRepository extends JpaRepository<ProfessionalCategory, Long> {}
