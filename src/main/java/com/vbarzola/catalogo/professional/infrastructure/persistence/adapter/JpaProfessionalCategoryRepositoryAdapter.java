package com.vbarzola.catalogo.professional.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.out.ProfessionalCategoryRepository;
import com.vbarzola.catalogo.professional.infrastructure.persistence.mapper.ProfessionalCategoryMapper;
import com.vbarzola.catalogo.professional.infrastructure.persistence.repository.JpaProfessionalCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaProfessionalCategoryRepositoryAdapter implements ProfessionalCategoryRepository {

    private final JpaProfessionalCategoryRepository jpaRepository;
    private final ProfessionalCategoryMapper mapper;

    @Override
    public List<ProfessionalCategory> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomainModel).toList();
    }

    @Override
    public Optional<ProfessionalCategory> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public ProfessionalCategory save(ProfessionalCategory professionalCategory) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(professionalCategory)));
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
}
