package com.vbarzola.catalogo.adapter.out.catedra;

import com.vbarzola.catalogo.adapter.out.catedra.dto.CatedraSnapshotResponse;
import com.vbarzola.catalogo.config.ApplicationProperties;
import com.vbarzola.catalogo.domain.model.CatalogSnapshot;
import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.domain.model.enumeration.CatalogDayOfWeek;
import com.vbarzola.catalogo.domain.port.out.CatedraSnapshotClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// TODO: Verificar endpoint exacto y formato de respuesta contra la documentación de la API de cátedra
@Component
public class CatedraSnapshotRestAdapter implements CatedraSnapshotClient {

    private static final Logger LOG = LoggerFactory.getLogger(CatedraSnapshotRestAdapter.class);

    private static final String SNAPSHOT_PATH = "/api/catalog";

    private final RestClient restClient;

    public CatedraSnapshotRestAdapter(ApplicationProperties props) {
        ApplicationProperties.Catedra catedra = props.getCatedra();

        RestClient.Builder builder = RestClient.builder().baseUrl(catedra.getRestBaseUrl());

        String jwt = catedra.getCatedraJwt();
        if (jwt != null && !jwt.isBlank()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        }

        this.restClient = builder.build();
    }

    @Override
    public CatalogSnapshot fetchSnapshot() {
        LOG.debug("Fetching full catalog snapshot from catedra: {}", SNAPSHOT_PATH);

        CatedraSnapshotResponse response = restClient.get().uri(SNAPSHOT_PATH).retrieve().body(CatedraSnapshotResponse.class);

        return mapToDomain(response);
    }

    private CatalogSnapshot mapToDomain(CatedraSnapshotResponse response) {
        long version = response.getVersion() != null ? response.getVersion() : 0L;

        List<ProfessionalCategory> categories = mapCategories(response.getCategories());
        Map<Long, ProfessionalCategory> categoryById = categories
            .stream()
            .collect(Collectors.toMap(ProfessionalCategory::getId, Function.identity()));

        List<Professional> professionals = mapProfessionals(response.getProfessionals(), categoryById);
        Map<Long, Professional> professionalById = professionals
            .stream()
            .collect(Collectors.toMap(Professional::getId, Function.identity()));

        List<WeeklySchedule> schedules = mapSchedules(response.getSchedules(), professionalById);

        return new CatalogSnapshot(version, categories, professionals, schedules);
    }

    private List<ProfessionalCategory> mapCategories(List<CatedraSnapshotResponse.CategoryDTO> dtos) {
        if (dtos == null) return List.of();
        List<ProfessionalCategory> result = new ArrayList<>();
        for (CatedraSnapshotResponse.CategoryDTO dto : dtos) {
            ProfessionalCategory cat = new ProfessionalCategory();
            cat.setId(dto.getId());
            cat.setName(dto.getName());
            cat.setDescription(dto.getDescription());
            cat.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
            cat.setCreatedAt(dto.getCreatedAt());
            cat.setUpdatedAt(dto.getUpdatedAt());
            result.add(cat);
        }
        return result;
    }

    private List<Professional> mapProfessionals(
        List<CatedraSnapshotResponse.ProfessionalDTO> dtos,
        Map<Long, ProfessionalCategory> categoryById
    ) {
        if (dtos == null) return List.of();
        List<Professional> result = new ArrayList<>();
        for (CatedraSnapshotResponse.ProfessionalDTO dto : dtos) {
            ProfessionalCategory category = categoryById.get(dto.getCategoryId());
            if (category == null) {
                LOG.warn("Skipping professional id={}: category id={} not found in snapshot", dto.getId(), dto.getCategoryId());
                continue;
            }
            Professional professional = new Professional();
            professional.setId(dto.getId());
            professional.setFirstName(dto.getFirstName());
            professional.setLastName(dto.getLastName());
            professional.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
            professional.setCategory(category);
            professional.setCreatedAt(dto.getCreatedAt());
            professional.setUpdatedAt(dto.getUpdatedAt());
            result.add(professional);
        }
        return result;
    }

    private List<WeeklySchedule> mapSchedules(List<CatedraSnapshotResponse.ScheduleDTO> dtos, Map<Long, Professional> professionalById) {
        if (dtos == null) return List.of();
        List<WeeklySchedule> result = new ArrayList<>();
        for (CatedraSnapshotResponse.ScheduleDTO dto : dtos) {
            Professional professional = professionalById.get(dto.getProfessionalId());
            if (professional == null) {
                LOG.warn("Skipping schedule id={}: professional id={} not found in snapshot", dto.getId(), dto.getProfessionalId());
                continue;
            }
            WeeklySchedule schedule = new WeeklySchedule();
            schedule.setId(dto.getId());
            schedule.setProfessional(professional);
            schedule.setDayOfWeek(CatalogDayOfWeek.valueOf(dto.getDayOfWeek()));
            schedule.setStartTime(dto.getStartTime());
            schedule.setEndTime(dto.getEndTime());
            schedule.setSlotDurationMinutes(dto.getSlotDurationMinutes());
            schedule.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
            schedule.setCreatedAt(dto.getCreatedAt());
            schedule.setUpdatedAt(dto.getUpdatedAt());
            result.add(schedule);
        }
        return result;
    }
}
