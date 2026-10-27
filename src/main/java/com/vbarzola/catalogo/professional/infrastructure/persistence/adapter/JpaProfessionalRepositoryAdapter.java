package com.vbarzola.catalogo.professional.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalRepository;
import com.vbarzola.catalogo.professional.infrastructure.persistence.mapper.ProfessionalMapper;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaProfessionalRepositoryAdapter implements ProfessionalRepository {

    private final JpaProfessionalRepository jpaRepository;
    private final ProfessionalMapper mapper;

    @Override
    public Page<Professional> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomainModel);
    }

    @Override
    public Page<Professional> findAllWithEagerRelationships(Pageable pageable) {
        return jpaRepository.findAllWithToOneRelationships(pageable).map(mapper::toDomainModel);
    }

    @Override
    public Optional<Professional> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public Optional<Professional> findOneWithEagerRelationships(Long id) {
        return jpaRepository.findOneWithToOneRelationships(id).map(mapper::toDomainModel);
    }

    @Override
    public Professional save(Professional professional) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(professional)));
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
    public Page<Professional> search(String name, Long categoryId, Boolean enabled, Pageable pageable) {
        return jpaRepository.search(name, categoryId, enabled, pageable).map(mapper::toDomainModel);
    }
}
