package com.vbarzola.catalogo.adapter.out.persistence;

import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.domain.port.out.WeeklyScheduleRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyScheduleJpaRepository extends JpaRepository<WeeklySchedule, Long>, WeeklyScheduleRepositoryPort {
    @Override
    Optional<WeeklySchedule> findById(Long id);

    @Override
    default Optional<WeeklySchedule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    @Override
    default Page<WeeklySchedule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select weeklySchedule from WeeklySchedule weeklySchedule left join fetch weeklySchedule.professional",
        countQuery = "select count(weeklySchedule) from WeeklySchedule weeklySchedule"
    )
    Page<WeeklySchedule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select weeklySchedule from WeeklySchedule weeklySchedule left join fetch weeklySchedule.professional")
    List<WeeklySchedule> findAllWithToOneRelationships();

    @Query(
        "select weeklySchedule from WeeklySchedule weeklySchedule left join fetch weeklySchedule.professional where weeklySchedule.id =:id"
    )
    Optional<WeeklySchedule> findOneWithToOneRelationships(@Param("id") Long id);

    @Override
    @Query(
        "select ws from WeeklySchedule ws left join fetch ws.professional p left join fetch p.category" + " where p.id = :professionalId"
    )
    List<WeeklySchedule> findByProfessionalId(@Param("professionalId") Long professionalId);
}
