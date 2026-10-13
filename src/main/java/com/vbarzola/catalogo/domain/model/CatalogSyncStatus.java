package com.vbarzola.catalogo.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Single-row table tracking the catalog synchronization state.
 * ID is always 1. Use CatalogSyncStatusRepository.findSingleton().
 */
@Entity
@Table(name = "catalog_sync_status")
public class CatalogSyncStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Long SINGLETON_ID = 1L;

    @Id
    @Column(name = "id")
    private Long id = SINGLETON_ID;

    @NotNull
    @Column(name = "local_version", nullable = false)
    private Long localVersion;

    @NotNull
    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @Size(max = 20)
    @Column(name = "sync_type", length = 20)
    private String syncType;

    @Size(max = 1000)
    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocalVersion() {
        return localVersion;
    }

    public void setLocalVersion(Long localVersion) {
        this.localVersion = localVersion;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "CatalogSyncStatus{" + "localVersion=" + localVersion + ", appliedAt=" + appliedAt + ", syncType='" + syncType + "'" + '}';
    }
}
