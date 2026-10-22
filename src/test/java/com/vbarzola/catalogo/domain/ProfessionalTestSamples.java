package com.vbarzola.catalogo.domain;

import com.vbarzola.catalogo.domain.model.Professional;
import com.vbarzola.catalogo.domain.model.ProfessionalCategory;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Professional getProfessionalSample1() {
        return new Professional().id(1L).firstName("firstName1").lastName("lastName1");
    }

    public static Professional getProfessionalSample2() {
        return new Professional().id(2L).firstName("firstName2").lastName("lastName2");
    }

    public static Professional getProfessionalRandomSampleGenerator() {
        return new Professional()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString());
    }
}
