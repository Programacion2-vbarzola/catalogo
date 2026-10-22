package com.vbarzola.catalogo.adapter.out.persistence;

import com.vbarzola.catalogo.domain.model.CatalogSyncStatus;
import com.vbarzola.catalogo.domain.port.out.CatalogSyncStatusRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogSyncStatusJpaRepository extends JpaRepository<CatalogSyncStatus, Long>, CatalogSyncStatusRepositoryPort {
    @Override
    default java.util.Optional<CatalogSyncStatus> findSingleton() {
        return findById(CatalogSyncStatus.SINGLETON_ID);
    }
}
