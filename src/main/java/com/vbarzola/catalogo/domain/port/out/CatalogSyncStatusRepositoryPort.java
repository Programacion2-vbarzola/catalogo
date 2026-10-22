package com.vbarzola.catalogo.domain.port.out;

import com.vbarzola.catalogo.domain.model.CatalogSyncStatus;
import java.util.Optional;

public interface CatalogSyncStatusRepositoryPort {
    Optional<CatalogSyncStatus> findSingleton();

    CatalogSyncStatus save(CatalogSyncStatus entity);

    boolean existsById(Long id);
}
