package com.vbarzola.catalogo.sync.application.service;

import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogSyncService {

    private final SyncFullCatalogUseCase syncFullCatalogUseCase;

    public void syncFullCatalog() {
        syncFullCatalogUseCase.syncFullCatalog();
    }
}
