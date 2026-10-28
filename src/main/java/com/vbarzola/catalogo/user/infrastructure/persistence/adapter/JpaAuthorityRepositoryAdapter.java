package com.vbarzola.catalogo.user.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.user.domain.model.Authority;
import com.vbarzola.catalogo.user.domain.ports.out.AuthorityRepository;
import com.vbarzola.catalogo.user.infrastructure.persistence.mapper.AuthorityMapper;
import com.vbarzola.catalogo.user.infrastructure.persistence.repository.JpaAuthorityRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaAuthorityRepositoryAdapter implements AuthorityRepository {

    private final JpaAuthorityRepository jpaRepository;
    private final AuthorityMapper mapper;

    @Override
    public List<Authority> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomainModel).toList();
    }

    @Override
    public Optional<Authority> findById(String name) {
        return jpaRepository.findById(name).map(mapper::toDomainModel);
    }

    @Override
    public Authority save(Authority authority) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(authority)));
    }

    @Override
    public boolean existsById(String name) {
        return jpaRepository.existsById(name);
    }

    @Override
    public void deleteById(String name) {
        jpaRepository.deleteById(name);
    }
}
