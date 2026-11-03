package com.vbarzola.catalogo.sync.infrastructure.web.controller;

import com.vbarzola.catalogo.sync.application.service.CatalogSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Tag(name = "Catalog Sync", description = "Sincronización manual del catálogo")
public class CatalogSyncController {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogSyncController.class);

    private final CatalogSyncService catalogSyncService;

    @PostMapping("/catalog/full")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Dispara sincronización completa del catálogo desde la cátedra")
    public ResponseEntity<Void> triggerFullSync() {
        LOG.info("Manual full catalog sync triggered");
        catalogSyncService.syncFullCatalog();
        return ResponseEntity.noContent().build();
    }
}
