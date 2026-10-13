package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.domain.mapper.WeeklyScheduleMapper;
import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.domain.port.in.WeeklyScheduleUseCase;
import com.vbarzola.catalogo.domain.port.out.WeeklyScheduleRepositoryPort;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeeklyScheduleService implements WeeklyScheduleUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklyScheduleService.class);

    private final WeeklyScheduleRepositoryPort repository;
    private final WeeklyScheduleMapper mapper;

    public WeeklyScheduleService(WeeklyScheduleRepositoryPort repository, WeeklyScheduleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public WeeklyScheduleDTO save(WeeklyScheduleDTO dto) {
        LOG.debug("Request to save WeeklySchedule : {}", dto);
        WeeklySchedule entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public WeeklyScheduleDTO update(WeeklyScheduleDTO dto) {
        LOG.debug("Request to update WeeklySchedule : {}", dto);
        WeeklySchedule entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<WeeklyScheduleDTO> partialUpdate(WeeklyScheduleDTO dto) {
        LOG.debug("Request to partially update WeeklySchedule : {}", dto);
        return repository
            .findById(dto.getId())
            .map(existing -> {
                mapper.partialUpdate(existing, dto);
                return existing;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyScheduleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WeeklySchedules");
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<WeeklyScheduleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return repository.findAllWithEagerRelationships(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeeklyScheduleDTO> findOne(Long id) {
        LOG.debug("Request to get WeeklySchedule : {}", id);
        return repository.findOneWithEagerRelationships(id).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WeeklySchedule : {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
