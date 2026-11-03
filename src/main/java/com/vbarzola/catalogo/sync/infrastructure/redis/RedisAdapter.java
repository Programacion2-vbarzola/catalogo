package com.vbarzola.catalogo.sync.infrastructure.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.config.ApplicationProperties;
import com.vbarzola.catalogo.professional.domain.model.Professional;
import com.vbarzola.catalogo.professional.domain.model.ProfessionalCategory;
import com.vbarzola.catalogo.schedule.domain.model.WeeklySchedule;
import com.vbarzola.catalogo.schedule.domain.model.enumeration.CatalogDayOfWeek;
import com.vbarzola.catalogo.sync.domain.model.CatalogChange;
import com.vbarzola.catalogo.sync.domain.ports.out.CatalogChangesClient;
import com.vbarzola.catalogo.sync.infrastructure.redis.dto.CatalogChangeRedisDto;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisAdapter implements CatalogChangesClient {

    private static final Logger LOG = LoggerFactory.getLogger(RedisAdapter.class);

    private static final String KEY_OLDEST_VERSION = "catedra:sync:oldest-available-version";
    private static final String KEY_CHANGES_PREFIX = "catedra:sync:changes:";
    private static final String HASH_CATEGORIES = "catedra:sync:professional-categories";
    private static final String HASH_PROFESSIONALS = "catedra:sync:professionals";
    private static final String HASH_SCHEDULES = "catedra:sync:weekly-schedules";

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    public RedisAdapter(ApplicationProperties props, ObjectMapper objectMapper) {
        ApplicationProperties.Catedra catedra = props.getCatedra();
        this.objectMapper = objectMapper;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);

        String host = catedra.getRedisHost();
        int port = catedra.getRedisPort();
        String user = catedra.getRedisUsername();
        String password = catedra.getRedisPassword();

        if (user != null && !user.isBlank()) {
            this.jedisPool = new JedisPool(poolConfig, host, port, 2000, user, password);
        } else {
            this.jedisPool = new JedisPool(poolConfig, host, port);
        }
    }

    @Override
    public long getOldestAvailableVersion() {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(KEY_OLDEST_VERSION);
            return value != null ? Long.parseLong(value.trim()) : 0L;
        }
    }

    @Override
    public CatalogChange fetchChanges(long version) {
        try (Jedis jedis = jedisPool.getResource()) {
            String changesJson = jedis.get(KEY_CHANGES_PREFIX + version);
            if (changesJson == null) {
                LOG.warn("No changes found in Redis for version={}", version);
                return new CatalogChange(version, List.of(), List.of(), List.of());
            }

            CatalogChangeRedisDto dto = objectMapper.readValue(changesJson, CatalogChangeRedisDto.class);
            CatalogChangeRedisDto.Changes ids = dto.getChanges();

            List<ProfessionalCategory> categories = fetchCategories(jedis, ids.getProfessionalCategories());
            List<Professional> professionals = fetchProfessionals(jedis, ids.getProfessionals());
            List<WeeklySchedule> schedules = fetchSchedules(jedis, ids.getWeeklySchedules());

            return new CatalogChange(version, categories, professionals, schedules);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch catalog changes from Redis for version=" + version, e);
        }
    }

    private List<ProfessionalCategory> fetchCategories(Jedis jedis, List<Long> ids) throws Exception {
        if (ids == null || ids.isEmpty()) return List.of();
        List<ProfessionalCategory> result = new ArrayList<>();
        for (Long id : ids) {
            String json = jedis.hget(HASH_CATEGORIES, String.valueOf(id));
            if (json == null) {
                LOG.warn("Category id={} not found in Redis hash", id);
                continue;
            }
            CategoryDto dto = objectMapper.readValue(json, CategoryDto.class);
            ProfessionalCategory cat = new ProfessionalCategory();
            cat.setId(dto.id);
            cat.setName(dto.name);
            cat.setDescription(dto.description);
            cat.setEnabled(dto.enabled != null ? dto.enabled : true);
            cat.setCreatedAt(dto.createdAt);
            cat.setUpdatedAt(dto.updatedAt);
            result.add(cat);
        }
        return result;
    }

    private List<Professional> fetchProfessionals(Jedis jedis, List<Long> ids) throws Exception {
        if (ids == null || ids.isEmpty()) return List.of();
        List<Professional> result = new ArrayList<>();
        for (Long id : ids) {
            String json = jedis.hget(HASH_PROFESSIONALS, String.valueOf(id));
            if (json == null) {
                LOG.warn("Professional id={} not found in Redis hash", id);
                continue;
            }
            ProfessionalDto dto = objectMapper.readValue(json, ProfessionalDto.class);

            String catJson = jedis.hget(HASH_CATEGORIES, String.valueOf(dto.categoryId));
            if (catJson == null) {
                LOG.warn("Category id={} for professional id={} not found in Redis", dto.categoryId, id);
                continue;
            }
            CategoryDto catDto = objectMapper.readValue(catJson, CategoryDto.class);

            ProfessionalCategory category = new ProfessionalCategory();
            category.setId(catDto.id);
            category.setName(catDto.name);
            category.setDescription(catDto.description);
            category.setEnabled(catDto.enabled != null ? catDto.enabled : true);

            Professional prof = new Professional();
            prof.setId(dto.id);
            prof.setFirstName(dto.firstName);
            prof.setLastName(dto.lastName);
            prof.setEnabled(dto.enabled != null ? dto.enabled : true);
            prof.setCategory(category);
            prof.setCreatedAt(dto.createdAt);
            prof.setUpdatedAt(dto.updatedAt);
            result.add(prof);
        }
        return result;
    }

    private List<WeeklySchedule> fetchSchedules(Jedis jedis, List<Long> ids) throws Exception {
        if (ids == null || ids.isEmpty()) return List.of();
        List<WeeklySchedule> result = new ArrayList<>();
        for (Long id : ids) {
            String json = jedis.hget(HASH_SCHEDULES, String.valueOf(id));
            if (json == null) {
                LOG.warn("Schedule id={} not found in Redis hash", id);
                continue;
            }
            ScheduleDto dto = objectMapper.readValue(json, ScheduleDto.class);

            String profJson = jedis.hget(HASH_PROFESSIONALS, String.valueOf(dto.professionalId));
            if (profJson == null) {
                LOG.warn("Professional id={} for schedule id={} not found in Redis", dto.professionalId, id);
                continue;
            }
            ProfessionalDto profDto = objectMapper.readValue(profJson, ProfessionalDto.class);

            ProfessionalCategory category = new ProfessionalCategory();
            String catJson = jedis.hget(HASH_CATEGORIES, String.valueOf(profDto.categoryId));
            if (catJson != null) {
                CategoryDto catDto = objectMapper.readValue(catJson, CategoryDto.class);
                category.setId(catDto.id);
                category.setName(catDto.name);
                category.setDescription(catDto.description);
                category.setEnabled(catDto.enabled != null ? catDto.enabled : true);
            } else {
                category.setId(profDto.categoryId);
            }

            Professional professional = new Professional();
            professional.setId(profDto.id);
            professional.setFirstName(profDto.firstName);
            professional.setLastName(profDto.lastName);
            professional.setEnabled(profDto.enabled != null ? profDto.enabled : true);
            professional.setCategory(category);

            WeeklySchedule schedule = new WeeklySchedule();
            schedule.setId(dto.id);
            schedule.setProfessional(professional);
            schedule.setDayOfWeek(CatalogDayOfWeek.valueOf(dto.dayOfWeek));
            schedule.setStartTime(dto.startTime);
            schedule.setEndTime(dto.endTime);
            schedule.setSlotDurationMinutes(dto.slotDurationMinutes);
            schedule.setEnabled(dto.enabled != null ? dto.enabled : true);
            schedule.setCreatedAt(dto.createdAt);
            schedule.setUpdatedAt(dto.updatedAt);
            result.add(schedule);
        }
        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CategoryDto {

        public Long id;
        public String name;
        public String description;
        public Boolean enabled;
        public Instant createdAt;
        public Instant updatedAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ProfessionalDto {

        public Long id;
        public String firstName;
        public String lastName;
        public Boolean enabled;
        public Long categoryId;
        public Instant createdAt;
        public Instant updatedAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ScheduleDto {

        public Long id;
        public Long professionalId;
        public String dayOfWeek;
        public String startTime;
        public String endTime;
        public Integer slotDurationMinutes;
        public Boolean enabled;
        public Instant createdAt;
        public Instant updatedAt;
    }
}
