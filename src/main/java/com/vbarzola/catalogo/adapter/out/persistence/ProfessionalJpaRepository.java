package com.vbarzola.catalogo.adapter.out.persistence;

import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.port.out.ProfessionalRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalJpaRepository extends JpaRepository<Professional, Long>, ProfessionalRepositoryPort {
    @Override
    Optional<Professional> findById(Long id);

    @Override
    default Optional<Professional> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    @Override
    default Page<Professional> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professional from Professional professional left join fetch professional.category",
        countQuery = "select count(professional) from Professional professional"
    )
    Page<Professional> findAllWithToOneRelationships(Pageable pageable);

    @Query("select professional from Professional professional left join fetch professional.category")
    List<Professional> findAllWithToOneRelationships();

    @Query("select professional from Professional professional left join fetch professional.category where professional.id =:id")
    Optional<Professional> findOneWithToOneRelationships(@Param("id") Long id);
}
