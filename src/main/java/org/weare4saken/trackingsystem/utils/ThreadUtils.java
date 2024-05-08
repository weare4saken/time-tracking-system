package org.weare4saken.trackingsystem.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class ThreadUtils {
    public void sleep(long millis, TimeUnit unit) {
        try {
            unit.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
