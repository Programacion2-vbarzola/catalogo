package com.vbarzola.catalogo.schedule.infrastructure.persistence.repository;

import com.vbarzola.catalogo.schedule.infrastructure.persistence.entity.WeeklyScheduleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWeeklyScheduleRepository extends JpaRepository<WeeklyScheduleEntity, Long> {
    @Query(
        value = "select weeklySchedule from WeeklyScheduleEntity weeklySchedule left join fetch weeklySchedule.professional",
        countQuery = "select count(weeklySchedule) from WeeklyScheduleEntity weeklySchedule"
    )
    Page<WeeklyScheduleEntity> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select weeklySchedule from WeeklyScheduleEntity weeklySchedule left join fetch weeklySchedule.professional where weeklySchedule.id =:id"
    )
    Optional<WeeklyScheduleEntity> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select ws from WeeklyScheduleEntity ws left join fetch ws.professional p left join fetch p.category where p.id = :professionalId"
    )
    List<WeeklyScheduleEntity> findByProfessionalId(@Param("professionalId") Long professionalId);
}
