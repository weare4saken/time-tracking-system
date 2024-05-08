package org.weare4saken.trackingsystem.aspect.tracker;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.weare4saken.trackingsystem.aspect.data.impl.TrackingSyncTimeAnnotationData;
import org.weare4saken.trackingsystem.service.TrackingTimeStatisticsService;

@Component
public class SyncTimeTracker extends TimeTracker{

    public SyncTimeTracker(TrackingTimeStatisticsService trackingTimeStatisticsService) {
        super(trackingTimeStatisticsService);
    }

    @Override
    protected Object bind(Object result, ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch, Throwable throwable) {
        this.recordStatistics(proceedingJoinPoint, stopWatch, new TrackingSyncTimeAnnotationData(proceedingJoinPoint), throwable);
        return result;
    }
}

