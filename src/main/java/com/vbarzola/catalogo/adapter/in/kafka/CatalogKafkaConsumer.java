package com.vbarzola.catalogo.adapter.in.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Kafka adapter for consuming catalog synchronization events from cátedra.
 * Sprint 1: implement consumption of CatalogUpdated events from the cátedra topic.
 */
@Component
public class CatalogKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogKafkaConsumer.class);

    public void handleCatalogUpdated(String message) {
        LOG.debug("CatalogUpdated event received: {}", message);
        // TODO Sprint 1: parse event, call domain use case to apply incremental sync
    }
}
