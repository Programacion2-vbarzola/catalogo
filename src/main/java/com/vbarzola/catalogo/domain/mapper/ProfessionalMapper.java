package com.vbarzola.catalogo.domain.mapper;

import com.vbarzola.catalogo.domain.dto.ProfessionalCategoryDTO;
import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professional} and its DTO {@link ProfessionalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalMapper extends EntityMapper<ProfessionalDTO, Professional> {
    @Mapping(target = "category", source = "category", qualifiedByName = "professionalCategoryName")
    ProfessionalDTO toDto(Professional s);

    @Named("professionalCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProfessionalCategoryDTO toDtoProfessionalCategoryName(ProfessionalCategory professionalCategory);
}
