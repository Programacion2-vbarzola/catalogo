package com.vbarzola.catalogo.schedule.application.exception;

public class WeeklyScheduleException extends RuntimeException {

    public WeeklyScheduleException(Long id) {
        super("Could not find weekly schedule with id: " + id);
    }
}
