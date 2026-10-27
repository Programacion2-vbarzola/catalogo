package com.vbarzola.catalogo.sync.infrastructure.persistence.repository;

import com.vbarzola.catalogo.sync.infrastructure.persistence.entity.CatalogSyncStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCatalogSyncStatusRepository extends JpaRepository<CatalogSyncStatusEntity, Long> {
    default Optional<CatalogSyncStatusEntity> findSingleton() {
        return findById(CatalogSyncStatusEntity.SINGLETON_ID);
    }
}
