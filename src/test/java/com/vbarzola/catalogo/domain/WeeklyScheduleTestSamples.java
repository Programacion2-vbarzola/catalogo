package com.vbarzola.catalogo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WeeklyScheduleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WeeklySchedule getWeeklyScheduleSample1() {
        return new WeeklySchedule().id(1L).startTime("startTime1").endTime("endTime1").slotDurationMinutes(1);
    }

    public static WeeklySchedule getWeeklyScheduleSample2() {
        return new WeeklySchedule().id(2L).startTime("startTime2").endTime("endTime2").slotDurationMinutes(2);
    }

    public static WeeklySchedule getWeeklyScheduleRandomSampleGenerator() {
        return new WeeklySchedule()
            .id(longCount.incrementAndGet())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString())
            .slotDurationMinutes(intCount.incrementAndGet());
    }
}
