package com.vbarzola.catalogo.professional.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalCategoryMapper {

    public ProfessionalCategoryEntity toEntity(ProfessionalCategory domain) {
        if (domain == null) {
            return null;
        }
        return ProfessionalCategoryEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }

    public ProfessionalCategory toDomainModel(ProfessionalCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return ProfessionalCategory.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .enabled(entity.getEnabled())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
