package org.weare4saken.trackingsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.weare4saken.trackingsystem.aspect.data.ProceedingBinder;

@Component
@Aspect
@Slf4j
public class TrackingTimeAspect {

    private final ProceedingBinder timeTracker;
    private final ProceedingBinder timeAsyncTracker;

    public TrackingTimeAspect(@Qualifier("syncTimeTracker") ProceedingBinder timeTracker,
                              @Qualifier("asyncTimeTracker") ProceedingBinder timeAsyncTracker) {
        this.timeTracker = timeTracker;
        this.timeAsyncTracker = timeAsyncTracker;
    }

    @Pointcut("@annotation(org.weare4saken.trackingsystem.annotation.TrackTime) && " +
            "!@annotation(org.weare4saken.trackingsystem.annotation.TrackAsyncTime)")
    private void trackTimeAspect() {
    }

    @Pointcut("@annotation(org.weare4saken.trackingsystem.annotation.TrackAsyncTime) && " +
            "!@annotation(org.weare4saken.trackingsystem.annotation.TrackTime)")
    private void trackAsyncTimeAspect() {
    }

    @Around("trackTimeAspect()")
    public Object trackTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.timeTracker.bind(proceedingJoinPoint);
    }

    @Around("trackAsyncTimeAspect()")
    public Object trackAsyncTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.timeAsyncTracker.bind(proceedingJoinPoint);
    }
}
