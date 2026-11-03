package com.vbarzola.catalogo.sync.domain.ports.in;

public interface ProcessCatalogUpdateUseCase {
    void process(String eventId, long newVersion);
}
