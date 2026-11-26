package com.vbarzola.catalogo.sync.application.exception;

public class SnapshotCorruptException extends RuntimeException {

    public SnapshotCorruptException(String message) {
        super(message);
    }
}
