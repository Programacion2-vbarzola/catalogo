package com.vbarzola.catalogo;

import com.vbarzola.catalogo.config.AsyncSyncConfiguration;
import com.vbarzola.catalogo.config.EmbeddedKafka;
import com.vbarzola.catalogo.config.EmbeddedSQL;
import com.vbarzola.catalogo.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CatalogoApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
