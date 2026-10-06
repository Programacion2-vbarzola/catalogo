package ar.edu.um.programacion2.catalogo.service.mapper;

import ar.edu.um.programacion2.catalogo.domain.Professional;
import ar.edu.um.programacion2.catalogo.domain.WeeklySchedule;
import ar.edu.um.programacion2.catalogo.service.dto.ProfessionalDTO;
import ar.edu.um.programacion2.catalogo.service.dto.WeeklyScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WeeklySchedule} and its DTO {@link WeeklyScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface WeeklyScheduleMapper extends EntityMapper<WeeklyScheduleDTO, WeeklySchedule> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalLastName")
    WeeklyScheduleDTO toDto(WeeklySchedule s);

    @Named("professionalLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    ProfessionalDTO toDtoProfessionalLastName(Professional professional);
}
