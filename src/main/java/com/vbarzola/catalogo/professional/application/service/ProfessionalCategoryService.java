package com.vbarzola.catalogo.professional.application.service;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalCategoryException;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalCategoriesUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalCategoryByIdUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessionalCategoryService {

    private final GetProfessionalCategoryByIdUseCase getProfessionalCategoryByIdUseCase;
    private final GetAllProfessionalCategoriesUseCase getAllProfessionalCategoriesUseCase;

    public ProfessionalCategory getProfessionalCategoryById(Long id) {
        return getProfessionalCategoryByIdUseCase.getProfessionalCategoryById(id).orElseThrow(() -> new ProfessionalCategoryException(id));
    }

    public List<ProfessionalCategory> getAllProfessionalCategories() {
        return getAllProfessionalCategoriesUseCase.getAllProfessionalCategories();
    }
}
