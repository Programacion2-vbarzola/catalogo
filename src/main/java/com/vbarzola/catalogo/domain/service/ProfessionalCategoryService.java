package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.dto.ProfessionalCategoryDTO;
import com.vbarzola.catalogo.domain.mapper.ProfessionalCategoryMapper;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.domain.port.in.ProfessionalCategoryUseCase;
import com.vbarzola.catalogo.domain.port.out.ProfessionalCategoryRepositoryPort;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfessionalCategoryService implements ProfessionalCategoryUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalCategoryService.class);

    private final ProfessionalCategoryRepositoryPort repository;
    private final ProfessionalCategoryMapper mapper;

    public ProfessionalCategoryService(ProfessionalCategoryRepositoryPort repository, ProfessionalCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProfessionalCategoryDTO save(ProfessionalCategoryDTO dto) {
        LOG.debug("Request to save ProfessionalCategory : {}", dto);
        ProfessionalCategory entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public ProfessionalCategoryDTO update(ProfessionalCategoryDTO dto) {
        LOG.debug("Request to update ProfessionalCategory : {}", dto);
        ProfessionalCategory entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<ProfessionalCategoryDTO> partialUpdate(ProfessionalCategoryDTO dto) {
        LOG.debug("Request to partially update ProfessionalCategory : {}", dto);
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
    public List<ProfessionalCategoryDTO> findAll() {
        LOG.debug("Request to get all ProfessionalCategories");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionalCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalCategory : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalCategory : {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
