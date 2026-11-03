package com.vbarzola.catalogo.config;

import java.time.Duration;
import javax.cache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.vbarzola.catalogo.user.infrastructure.persistence.entity.UserEntity.class.getName());
            createCache(cm, com.vbarzola.catalogo.user.infrastructure.persistence.entity.AuthorityEntity.class.getName());
            createCache(cm, com.vbarzola.catalogo.user.infrastructure.persistence.entity.UserEntity.class.getName() + ".authorities");
            createCache(cm, com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalEntity.class.getName());
            createCache(
                cm,
                com.vbarzola.catalogo.professional.infrastructure.persistence.entity.ProfessionalCategoryEntity.class.getName()
            );
            createCache(cm, com.vbarzola.catalogo.schedule.infrastructure.persistence.entity.WeeklyScheduleEntity.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
