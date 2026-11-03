package com.vbarzola.catalogo.sync.application.service;

import com.vbarzola.catalogo.sync.domain.ports.in.ProcessCatalogUpdateUseCase;
import com.vbarzola.catalogo.sync.domain.ports.in.SyncFullCatalogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogSyncService {

    private final SyncFullCatalogUseCase syncFullCatalogUseCase;
    private final ProcessCatalogUpdateUseCase processCatalogUpdateUseCase;

    public void syncFullCatalog() {
        syncFullCatalogUseCase.syncFullCatalog();
    }

    public void processCatalogUpdate(String eventId, long newVersion) {
        processCatalogUpdateUseCase.process(eventId, newVersion);
    }
}
