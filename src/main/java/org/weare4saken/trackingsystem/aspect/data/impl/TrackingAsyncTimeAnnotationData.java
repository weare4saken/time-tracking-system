package org.weare4saken.trackingsystem.aspect.data.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.weare4saken.trackingsystem.annotation.TrackAsyncTime;
import org.weare4saken.trackingsystem.aspect.data.TrackingTimeAnnotationData;

import java.lang.reflect.Method;

public class TrackingAsyncTimeAnnotationData implements TrackingTimeAnnotationData {

    private final TrackAsyncTime trackAsyncTime;

    public TrackingAsyncTimeAnnotationData(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!method.isAnnotationPresent(TrackAsyncTime.class)) {
            throw new IllegalStateException("Метод " + method.getName() + " не имеет аннотации @TrackAsyncTime ");
        }

        this.trackAsyncTime = method.getAnnotation(TrackAsyncTime.class);
    }

    @Override
    public String methodCategory() {
        return this.trackAsyncTime.methodCategory();
    }

    @Override
    public Boolean ignoreExceptions() {
        return this.trackAsyncTime.ignoreExceptions();
    }

    @Override
    public TrackAsyncTime getAnnotationName() {
        return this.trackAsyncTime;
    }
}
