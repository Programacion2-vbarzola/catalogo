package com.vbarzola.catalogo.sync.domain.ports.in;

import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import java.util.Optional;

public interface GetCatalogSyncStatusUseCase {
    Optional<CatalogSyncStatus> getStatus();
}
