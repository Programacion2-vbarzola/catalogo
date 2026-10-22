package com.vbarzola.catalogo.domain.port.out;

import com.vbarzola.catalogo.domain.model.CatalogSnapshot;

public interface CatedraSnapshotClient {
    CatalogSnapshot fetchSnapshot();
}
