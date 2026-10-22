package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.dto.WeeklyScheduleDTO;
import com.vbarzola.catalogo.domain.mapper.WeeklyScheduleMapper;
import com.vbarzola.catalogo.domain.port.in.GetWeeklySchedulesUseCase;
import com.vbarzola.catalogo.domain.port.out.WeeklyScheduleRepositoryPort;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetWeeklySchedulesService implements GetWeeklySchedulesUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(GetWeeklySchedulesService.class);

    private final WeeklyScheduleRepositoryPort repository;
    private final WeeklyScheduleMapper mapper;

    public GetWeeklySchedulesService(WeeklyScheduleRepositoryPort repository, WeeklyScheduleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<WeeklyScheduleDTO> getByProfessionalId(Long professionalId) {
        LOG.debug("Request to get WeeklySchedules for professionalId={}", professionalId);
        return repository.findByProfessionalId(professionalId).stream().map(mapper::toDto).toList();
    }
}
