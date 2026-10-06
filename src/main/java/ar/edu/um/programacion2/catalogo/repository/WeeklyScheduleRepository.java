package ar.edu.um.programacion2.catalogo.repository;

import ar.edu.um.programacion2.catalogo.domain.WeeklySchedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WeeklySchedule entity.
 */
@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
    default Optional<WeeklySchedule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WeeklySchedule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

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
}
