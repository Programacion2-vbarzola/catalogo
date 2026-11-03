package com.vbarzola.catalogo.sync.application.usecases;

import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.domain.ports.in.GetCatalogSyncStatusUseCase;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogSyncStatusRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCatalogSyncStatusUseCaseImpl implements GetCatalogSyncStatusUseCase {

    private final CatalogSyncStatusRepository syncStatusRepo;

    @Override
    public Optional<CatalogSyncStatus> getStatus() {
        return syncStatusRepo.findSingleton();
    }
}
