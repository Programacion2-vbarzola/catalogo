package com.vbarzola.catalogo.professional.application.service;

import com.vbarzola.catalogo.professional.application.exception.ProfessionalCategoryException;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.domain.ports.in.CreateProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.DeleteProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetAllProfessionalCategoriesUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.GetProfessionalCategoryByIdUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.PartialUpdateProfessionalCategoryUseCase;
import com.vbarzola.catalogo.professional.domain.ports.in.UpdateProfessionalCategoryUseCase;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessionalCategoryService {

    private final CreateProfessionalCategoryUseCase createProfessionalCategoryUseCase;
    private final UpdateProfessionalCategoryUseCase updateProfessionalCategoryUseCase;
    private final PartialUpdateProfessionalCategoryUseCase partialUpdateProfessionalCategoryUseCase;
    private final GetProfessionalCategoryByIdUseCase getProfessionalCategoryByIdUseCase;
    private final GetAllProfessionalCategoriesUseCase getAllProfessionalCategoriesUseCase;
    private final DeleteProfessionalCategoryUseCase deleteProfessionalCategoryUseCase;

    public ProfessionalCategory createProfessionalCategory(ProfessionalCategory professionalCategory) {
        return createProfessionalCategoryUseCase.createProfessionalCategory(professionalCategory);
    }

    public ProfessionalCategory getProfessionalCategoryById(Long id) {
        return getProfessionalCategoryByIdUseCase.getProfessionalCategoryById(id).orElseThrow(() -> new ProfessionalCategoryException(id));
    }

    public Optional<ProfessionalCategory> updateProfessionalCategory(Long id, ProfessionalCategory professionalCategory) {
        return updateProfessionalCategoryUseCase.updateProfessionalCategory(id, professionalCategory);
    }

    public Optional<ProfessionalCategory> partialUpdateProfessionalCategory(ProfessionalCategory professionalCategory) {
        return partialUpdateProfessionalCategoryUseCase.partialUpdateProfessionalCategory(professionalCategory);
    }

    public List<ProfessionalCategory> getAllProfessionalCategories() {
        return getAllProfessionalCategoriesUseCase.getAllProfessionalCategories();
    }

    public void deleteProfessionalCategory(Long id) {
        deleteProfessionalCategoryUseCase.deleteProfessionalCategory(id);
    }
}
