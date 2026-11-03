package com.vbarzola.catalogo.sync.infrastructure.web.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CatalogSyncStatusResponse {

    private Long localVersion;
    private Instant appliedAt;
    private String syncType;
    private String errorMessage;
}
