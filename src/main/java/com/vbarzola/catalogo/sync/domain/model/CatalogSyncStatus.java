package com.vbarzola.catalogo.sync.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogSyncStatus {

    public static final Long SINGLETON_ID = 1L;

    @Builder.Default
    private Long id = SINGLETON_ID;

    private Long localVersion;
    private Instant appliedAt;
    private String syncType;
    private String errorMessage;
}
