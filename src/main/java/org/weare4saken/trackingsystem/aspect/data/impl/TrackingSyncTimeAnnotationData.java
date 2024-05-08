package org.weare4saken.trackingsystem.aspect.data.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.weare4saken.trackingsystem.annotation.TrackTime;
import org.weare4saken.trackingsystem.aspect.data.TrackingTimeAnnotationData;

import java.lang.reflect.Method;

public class TrackingSyncTimeAnnotationData implements TrackingTimeAnnotationData {

    private final TrackTime trackTime;

    public TrackingSyncTimeAnnotationData(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!method.isAnnotationPresent(TrackTime.class)) {
            throw new IllegalStateException("Метод " + method.getName() + " не имеет анноатции @TrackTime");
        }

        this.trackTime = method.getAnnotation(TrackTime.class);
    }

    @Override
    public String methodCategory() {
        return this.trackTime.methodCategory();
    }

    @Override
    public Boolean ignoreExceptions() {
        return this.trackTime.ignoreExceptions();
    }

    @Override
    public TrackTime getAnnotationName() {
        return this.trackTime;
    }
}
