package com.vbarzola.catalogo.professional.infrastructure.web.mapper;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryRequest;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalCategoryDtoMapper {

    public ProfessionalCategory toDomain(ProfessionalCategoryRequest request) {
        if (request == null) {
            return null;
        }
        return ProfessionalCategory.builder()
            .name(request.getName())
            .description(request.getDescription())
            .enabled(request.getEnabled())
            .build();
    }

    public ProfessionalCategoryResponse toResponse(ProfessionalCategory domain) {
        if (domain == null) {
            return null;
        }
        return ProfessionalCategoryResponse.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
}
