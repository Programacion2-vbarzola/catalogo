package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import com.vbarzola.catalogo.domain.mapper.ProfessionalMapper;
import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.port.in.ProfessionalUseCase;
import com.vbarzola.catalogo.domain.port.out.ProfessionalRepositoryPort;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfessionalService implements ProfessionalUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalService.class);

    private final ProfessionalRepositoryPort repository;
    private final ProfessionalMapper mapper;

    public ProfessionalService(ProfessionalRepositoryPort repository, ProfessionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProfessionalDTO save(ProfessionalDTO dto) {
        LOG.debug("Request to save Professional : {}", dto);
        Professional entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public ProfessionalDTO update(ProfessionalDTO dto) {
        LOG.debug("Request to update Professional : {}", dto);
        Professional entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<ProfessionalDTO> partialUpdate(ProfessionalDTO dto) {
        LOG.debug("Request to partially update Professional : {}", dto);
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
    public Page<ProfessionalDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Professionals");
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<ProfessionalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return repository.findAllWithEagerRelationships(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionalDTO> findOne(Long id) {
        LOG.debug("Request to get Professional : {}", id);
        return repository.findOneWithEagerRelationships(id).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Professional : {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
