package org.weare4saken.trackingsystem.aspect.tracker;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.weare4saken.trackingsystem.aspect.data.impl.TrackingAsyncTimeAnnotationData;
import org.weare4saken.trackingsystem.service.TrackingTimeStatisticsService;

import java.util.concurrent.*;

@Component
public class AsyncTimeTracker extends TimeTracker{

    public AsyncTimeTracker(TrackingTimeStatisticsService trackingTimeStatisticsService) {
        super(trackingTimeStatisticsService);
    }

    @Override
    protected Object bind(Object result, ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch, Throwable throwable) {
        TrackingAsyncTimeAnnotationData annotationData = new TrackingAsyncTimeAnnotationData(proceedingJoinPoint);

        Future<?> future = this.tryBindToFuture(result, proceedingJoinPoint, stopWatch, annotationData);

        if (future != null) {
            return future;
        }

        this.recordStatistics(proceedingJoinPoint, stopWatch, annotationData, throwable);
        return result;
    }

    private CompletableFuture<?> tryBindToFuture(Object result, ProceedingJoinPoint joinPoint,
                                                 StopWatch stopWatch, TrackingAsyncTimeAnnotationData annotationData) {
        CompletableFuture<?> future = null;

        if (result instanceof CompletableFuture<?>) {
            future = ((CompletableFuture<?>) result);
        } else if (result instanceof Future<?>) {
            future = CompletableFuture.supplyAsync(() -> {
                try {
                    return ((Future<?>) result).get(5, TimeUnit.MINUTES);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    throw new CompletionException(e);
                }
            });
        }

        if (future != null) {
            return future.whenComplete((res, error) -> this.recordStatistics(joinPoint, stopWatch, annotationData, error));
        }

        return future;
    }
}