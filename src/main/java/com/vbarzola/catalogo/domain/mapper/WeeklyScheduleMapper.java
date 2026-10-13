package com.vbarzola.catalogo.domain.mapper;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.model.WeeklySchedule;
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
