package ar.edu.um.programacion2.catalogo.service.mapper;

import ar.edu.um.programacion2.catalogo.domain.Professional;
import ar.edu.um.programacion2.catalogo.domain.ProfessionalCategory;
import ar.edu.um.programacion2.catalogo.service.dto.ProfessionalCategoryDTO;
import ar.edu.um.programacion2.catalogo.service.dto.ProfessionalDTO;
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
