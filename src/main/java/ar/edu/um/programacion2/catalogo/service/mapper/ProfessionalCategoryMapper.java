package ar.edu.um.programacion2.catalogo.service.mapper;

import ar.edu.um.programacion2.catalogo.domain.ProfessionalCategory;
import ar.edu.um.programacion2.catalogo.service.dto.ProfessionalCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalCategory} and its DTO {@link ProfessionalCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalCategoryMapper extends EntityMapper<ProfessionalCategoryDTO, ProfessionalCategory> {}
