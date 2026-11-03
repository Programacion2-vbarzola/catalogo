package com.vbarzola.catalogo.sync.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbarzola.catalogo.sync.application.service.CatalogSyncService;
import com.vbarzola.catalogo.sync.infrastructure.messaging.dto.CatalogUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("!'${application.catedra.kafka-catalog-topic:}'.isEmpty()")
public class CatalogKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogKafkaConsumer.class);

    private final CatalogSyncService catalogSyncService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${application.catedra.kafka-catalog-topic}", containerFactory = "catedraKafkaListenerContainerFactory")
    public void handleCatalogUpdated(String message) {
        try {
            CatalogUpdatedEvent event = objectMapper.readValue(message, CatalogUpdatedEvent.class);
            LOG.debug("CatalogUpdated received: eventId={} newVersion={}", event.getEventId(), event.getNewVersion());
            catalogSyncService.processCatalogUpdate(event.getEventId(), event.getNewVersion());
        } catch (Exception e) {
            LOG.error("Failed to process CatalogUpdated event: {}", message, e);
        }
    }
}
