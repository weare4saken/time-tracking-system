package org.weare4saken.trackingsystem.rest.mapper;

import org.springframework.stereotype.Component;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeDto;

import java.util.function.Function;

@Component
public class TrackTimeEntityToDtoMapper {
    public Function<TrackingTimeStatistics, TrackTimeDto> toShort() {
        return stat -> TrackTimeDto.builder()
                .methodName(stat.getMethodName())
                .executionTime(stat.getExecutionTime()).build();
    }

    public Function<TrackingTimeStatistics, TrackTimeDto> toNormal() {
        return stat -> TrackTimeDto.builder()
                .methodCategory(stat.getMethodCategory())
                .returnType(stat.getReturnType())
                .packageName(stat.getPackageName())
                .className(stat.getClassName())
                .methodName(stat.getMethodName())
                .parameters(stat.getParameters())
                .executionTime(stat.getExecutionTime())
                .status(stat.getStatus())
                .createdAt(stat.getCreatedAt())
                .build();
    }
}