package com.vbarzola.catalogo.professional.infrastructure.persistence.repository;

import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProfessionalRepository extends JpaRepository<ProfessionalEntity, Long> {
    @Query(
        value = "select professional from ProfessionalEntity professional left join fetch professional.category",
        countQuery = "select count(professional) from ProfessionalEntity professional"
    )
    Page<ProfessionalEntity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select professional from ProfessionalEntity professional left join fetch professional.category where professional.id =:id")
    Optional<ProfessionalEntity> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "select p from ProfessionalEntity p left join fetch p.category c" +
        " where (cast(:name as String) is null or lower(concat(p.firstName, ' ', p.lastName)) like lower(concat('%', cast(:name as String), '%')))" +
        " and (:categoryId is null or c.id = :categoryId)" +
        " and (:enabled is null or p.enabled = :enabled)",
        countQuery = "select count(p) from ProfessionalEntity p left join p.category c" +
        " where (cast(:name as String) is null or lower(concat(p.firstName, ' ', p.lastName)) like lower(concat('%', cast(:name as String), '%')))" +
        " and (:categoryId is null or c.id = :categoryId)" +
        " and (:enabled is null or p.enabled = :enabled)"
    )
    Page<ProfessionalEntity> search(
        @Param("name") String name,
        @Param("categoryId") Long categoryId,
        @Param("enabled") Boolean enabled,
        Pageable pageable
    );
}
