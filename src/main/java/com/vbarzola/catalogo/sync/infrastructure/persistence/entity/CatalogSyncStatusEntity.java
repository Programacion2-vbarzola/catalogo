package com.vbarzola.catalogo.sync.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Single-row table tracking the catalog synchronization state.
 * ID is always 1. Use CatalogSyncStatusRepository.findSingleton().
 */
@Getter
@Setter
@Entity
@Table(name = "catalog_sync_status")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogSyncStatusEntity {

    public static final Long SINGLETON_ID = 1L;

    @Id
    @Column(name = "id")
    @Builder.Default
    private Long id = SINGLETON_ID;

    @Column(name = "local_version", nullable = false)
    private Long localVersion;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @Column(name = "sync_type", length = 20)
    private String syncType;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;
}
