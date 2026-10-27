package com.vbarzola.catalogo.professional.application.exception;

public class ProfessionalCategoryException extends RuntimeException {

    public ProfessionalCategoryException(Long id) {
        super("Could not find professional category with id: " + id);
    }
}
