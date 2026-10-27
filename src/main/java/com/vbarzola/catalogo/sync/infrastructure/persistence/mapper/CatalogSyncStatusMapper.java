package com.vbarzola.catalogo.sync.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.sync.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.sync.infrastructure.persistence.entity.CatalogSyncStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class CatalogSyncStatusMapper {

    public CatalogSyncStatusEntity toEntity(CatalogSyncStatus domain) {
        if (domain == null) {
            return null;
        }
        return CatalogSyncStatusEntity.builder()
            .id(domain.getId())
            .localVersion(domain.getLocalVersion())
            .appliedAt(domain.getAppliedAt())
            .syncType(domain.getSyncType())
            .errorMessage(domain.getErrorMessage())
            .build();
    }

    public CatalogSyncStatus toDomainModel(CatalogSyncStatusEntity entity) {
        if (entity == null) {
            return null;
        }
        return CatalogSyncStatus.builder()
            .id(entity.getId())
            .localVersion(entity.getLocalVersion())
            .appliedAt(entity.getAppliedAt())
            .syncType(entity.getSyncType())
            .errorMessage(entity.getErrorMessage())
            .build();
    }
}
