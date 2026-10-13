package com.vbarzola.catalogo.domain.mapper;

import com.vbarzola.catalogo.domain.dto.ProfessionalCategoryDTO;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalCategory} and its DTO {@link ProfessionalCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalCategoryMapper extends EntityMapper<ProfessionalCategoryDTO, ProfessionalCategory> {}
