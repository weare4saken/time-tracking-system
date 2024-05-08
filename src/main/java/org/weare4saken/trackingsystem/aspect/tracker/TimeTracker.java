package org.weare4saken.trackingsystem.aspect.tracker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.weare4saken.trackingsystem.aspect.data.ProceedingBinder;
import org.weare4saken.trackingsystem.aspect.data.TrackingTimeAnnotationData;
import org.weare4saken.trackingsystem.model.TrackTimeMethodStatus;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.service.TrackingTimeStatisticsService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class TimeTracker implements ProceedingBinder {

    private final TrackingTimeStatisticsService trackingTimeStatisticsService;

    public Object bind(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        Object result = null;
        Throwable throwable = null;

        try {
            stopWatch.start();
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            result = this.bind(result, proceedingJoinPoint, stopWatch, throwable);
        }

        return result;
    }

    protected abstract Object bind(Object result, ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch, Throwable throwable);

    protected void recordStatistics(ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch,
                                    TrackingTimeAnnotationData annotationData, Throwable throwable) {
        if (!stopWatch.isRunning()) {
            throw new IllegalStateException("StopWatch is not running");
        }

        stopWatch.stop();

        if (throwable != null && annotationData.ignoreExceptions()) {
            return;
        }

        TrackingTimeStatistics stats = this.buildStatistics(proceedingJoinPoint, stopWatch, annotationData, throwable);

        log.info("Execution time of {}.{}.{} :: {} ms [{}]",
                stats.getPackageName(),
                stats.getClassName(),
                stats.getMethodName(),
                stats.getExecutionTime(),
                stats.getStatus());

        this.trackingTimeStatisticsService.saveAsync(stats);
    }

    protected TrackingTimeStatistics buildStatistics(ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch,
                                                     TrackingTimeAnnotationData annotationData, Throwable t) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String methodCategory = annotationData.methodCategory();
        String returnType = methodSignature.getReturnType().getCanonicalName();
        String packageName = methodSignature.getDeclaringType().getPackageName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        long executionTime = stopWatch.getTotalTimeMillis();

        String parameters = Arrays.stream(methodSignature.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));

        return TrackingTimeStatistics.builder()
                .methodCategory(methodCategory)
                .returnType(returnType)
                .packageName(packageName)
                .className(className)
                .methodName(methodName)
                .parameters(parameters)
                .executionTime(executionTime)
                .status(t == null ? TrackTimeMethodStatus.COMPLETED : TrackTimeMethodStatus.EXCEPTION)
                .build();
    }
}
