package ar.edu.um.programacion2.catalogo.repository;

import ar.edu.um.programacion2.catalogo.domain.CatalogSyncStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogSyncStatusRepository extends JpaRepository<CatalogSyncStatus, Long> {
    default Optional<CatalogSyncStatus> findSingleton() {
        return findById(CatalogSyncStatus.SINGLETON_ID);
    }
}
