package org.weare4saken.trackingsystem.rest.mapper;

import org.springframework.stereotype.Component;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeDto;

import java.util.function.Function;

@Component
public class TrackTimeDtoToEntityMapper implements Function<TrackTimeDto, TrackingTimeStatistics> {
    @Override
    public TrackingTimeStatistics apply(TrackTimeDto requestDto) {
        return TrackingTimeStatistics.builder()
                .methodCategory(requestDto.getMethodCategory())
                .returnType(requestDto.getReturnType())
                .packageName(requestDto.getPackageName())
                .className(requestDto.getClassName())
                .methodName(requestDto.getMethodName())
                .parameters(requestDto.getParameters())
                .status(requestDto.getStatus())
                .createdAt(requestDto.getCreatedAt())
                .build();
    }
}