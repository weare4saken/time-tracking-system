package org.weare4saken.trackingsystem.aspect.data;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ProceedingBinder {
    Object bind(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;
}
