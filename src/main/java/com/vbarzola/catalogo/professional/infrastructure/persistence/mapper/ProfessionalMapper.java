package com.vbarzola.catalogo.professional.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessionalMapper {

    private final ProfessionalCategoryMapper categoryMapper;

    public ProfessionalEntity toEntity(Professional domain) {
        if (domain == null) {
            return null;
        }
        return ProfessionalEntity.builder()
            .id(domain.getId())
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .enabled(domain.getEnabled())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .category(categoryMapper.toEntity(domain.getCategory()))
            .build();
    }

    public Professional toDomainModel(ProfessionalEntity entity) {
        if (entity == null) {
            return null;
        }
        return Professional.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .enabled(entity.getEnabled())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .category(categoryMapper.toDomainModel(entity.getCategory()))
            .build();
    }
}
