package com.vbarzola.catalogo.schedule.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.ports.out.WeeklyScheduleRepository;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.mapper.WeeklyScheduleMapper;
import com.vbarzola.catalogo.schedule.infrastructure.persistence.repository.JpaWeeklyScheduleRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaWeeklyScheduleRepositoryAdapter implements WeeklyScheduleRepository {

    private final JpaWeeklyScheduleRepository jpaRepository;
    private final WeeklyScheduleMapper mapper;

    @Override
    public Page<WeeklySchedule> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomainModel);
    }

    @Override
    public Page<WeeklySchedule> findAllWithEagerRelationships(Pageable pageable) {
        return jpaRepository.findAllWithToOneRelationships(pageable).map(mapper::toDomainModel);
    }

    @Override
    public Optional<WeeklySchedule> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public Optional<WeeklySchedule> findOneWithEagerRelationships(Long id) {
        return jpaRepository.findOneWithToOneRelationships(id).map(mapper::toDomainModel);
    }

    @Override
    public WeeklySchedule save(WeeklySchedule weeklySchedule) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(weeklySchedule)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public List<WeeklySchedule> findByProfessionalId(Long professionalId) {
        return jpaRepository.findByProfessionalId(professionalId).stream().map(mapper::toDomainModel).toList();
    }
}
