package com.vbarzola.catalogo.domain.mapper;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity;
import com.vbarzola.catalogo.professional.infrastructure.web.dto.ProfessionalResponse;
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
    ProfessionalResponse toDtoProfessionalLastName(ProfessionalEntity professional);
}
