package com.vbarzola.catalogo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Catalogo.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Catedra catedra = new Catedra();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Catedra getCatedra() {
        return catedra;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Catedra {

        private String restBaseUrl;
        private String redisHost;
        private int redisPort = 6379;
        private String redisUsername;
        private String redisPassword;
        private String redisReadNamespace;
        private String redisWriteNamespace;
        private String kafkaBootstrapServers;
        private String kafkaConsumerGroupId;
        private String kafkaCatalogTopic;
        private String kafkaAppointmentActionsTopic;
        private String kafkaAppointmentPhoneTopic;

        public String getRestBaseUrl() {
            return restBaseUrl;
        }

        public void setRestBaseUrl(String restBaseUrl) {
            this.restBaseUrl = restBaseUrl;
        }

        public String getRedisHost() {
            return redisHost;
        }

        public void setRedisHost(String redisHost) {
            this.redisHost = redisHost;
        }

        public int getRedisPort() {
            return redisPort;
        }

        public void setRedisPort(int redisPort) {
            this.redisPort = redisPort;
        }

        public String getRedisUsername() {
            return redisUsername;
        }

        public void setRedisUsername(String redisUsername) {
            this.redisUsername = redisUsername;
        }

        public String getRedisPassword() {
            return redisPassword;
        }

        public void setRedisPassword(String redisPassword) {
            this.redisPassword = redisPassword;
        }

        public String getRedisReadNamespace() {
            return redisReadNamespace;
        }

        public void setRedisReadNamespace(String redisReadNamespace) {
            this.redisReadNamespace = redisReadNamespace;
        }

        public String getRedisWriteNamespace() {
            return redisWriteNamespace;
        }

        public void setRedisWriteNamespace(String redisWriteNamespace) {
            this.redisWriteNamespace = redisWriteNamespace;
        }

        public String getKafkaBootstrapServers() {
            return kafkaBootstrapServers;
        }

        public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
            this.kafkaBootstrapServers = kafkaBootstrapServers;
        }

        public String getKafkaConsumerGroupId() {
            return kafkaConsumerGroupId;
        }

        public void setKafkaConsumerGroupId(String kafkaConsumerGroupId) {
            this.kafkaConsumerGroupId = kafkaConsumerGroupId;
        }

        public String getKafkaCatalogTopic() {
            return kafkaCatalogTopic;
        }

        public void setKafkaCatalogTopic(String kafkaCatalogTopic) {
            this.kafkaCatalogTopic = kafkaCatalogTopic;
        }

        public String getKafkaAppointmentActionsTopic() {
            return kafkaAppointmentActionsTopic;
        }

        public void setKafkaAppointmentActionsTopic(String kafkaAppointmentActionsTopic) {
            this.kafkaAppointmentActionsTopic = kafkaAppointmentActionsTopic;
        }

        public String getKafkaAppointmentPhoneTopic() {
            return kafkaAppointmentPhoneTopic;
        }

        public void setKafkaAppointmentPhoneTopic(String kafkaAppointmentPhoneTopic) {
            this.kafkaAppointmentPhoneTopic = kafkaAppointmentPhoneTopic;
        }
    }
    // jhipster-needle-application-properties-property-class
}
