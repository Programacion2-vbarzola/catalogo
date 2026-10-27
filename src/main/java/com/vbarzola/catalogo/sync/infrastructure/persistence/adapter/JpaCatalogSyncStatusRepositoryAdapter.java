package com.vbarzola.catalogo.sync.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import com.vbarzola.catalogo.sync.infrastructure.persistence.mapper.CatalogSyncStatusMapper;
import com.vbarzola.catalogo.sync.infrastructure.persistence.repository.JpaCatalogSyncStatusRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaCatalogSyncStatusRepositoryAdapter implements CatalogSyncStatusRepository {

    private final JpaCatalogSyncStatusRepository jpaRepository;
    private final CatalogSyncStatusMapper mapper;

    @Override
    public Optional<CatalogSyncStatus> findSingleton() {
        return jpaRepository.findSingleton().map(mapper::toDomainModel);
    }

    @Override
    public CatalogSyncStatus save(CatalogSyncStatus catalogSyncStatus) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(catalogSyncStatus)));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
