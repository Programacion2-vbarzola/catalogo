package com.vbarzola.catalogo.professional.infrastructure.web.mapper;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalRequest;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessionalDtoMapper {

    private final ProfessionalCategoryDtoMapper categoryDtoMapper;

    public Professional toDomain(ProfessionalRequest request) {
        if (request == null) {
            return null;
        }
        ProfessionalCategory category = request.getCategoryId() != null
            ? ProfessionalCategory.builder().id(request.getCategoryId()).build()
            : null;
        return Professional.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .enabled(request.getEnabled())
            .category(category)
            .build();
    }

    public ProfessionalResponse toResponse(Professional domain) {
        if (domain == null) {
            return null;
        }
        return ProfessionalResponse.builder()
            .id(domain.getId())
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .category(categoryDtoMapper.toResponse(domain.getCategory()))
            .build();
    }
}
