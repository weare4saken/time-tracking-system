package org.weare4saken.trackingsystem.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.weare4saken.trackingsystem.model.TrackTimeMethodStatus;
import org.weare4saken.trackingsystem.rest.converter.CustomLocalDateDeserializer;
import org.weare4saken.trackingsystem.rest.converter.CustomLocalDateTimeDeserializer;
import org.weare4saken.trackingsystem.rest.converter.CustomTrackTimeMethodStatusDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackTimeDto {
    @JsonProperty("methodCategory")
    private String methodCategory;

    @JsonProperty("returnType")
    private String returnType;

    @JsonProperty("packageName")
    private String packageName;

    @JsonProperty("className")
    private String className;

    @JsonProperty("methodName")
    private String methodName;

    @JsonProperty("parameters")
    private String parameters;

    @JsonProperty(value = "executionTime", access = JsonProperty.Access.READ_ONLY)
    private Long executionTime;

    @JsonProperty(value = "status")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = CustomTrackTimeMethodStatusDeserializer.class)
    private TrackTimeMethodStatus status;

    @JsonProperty(value = "createdAt")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonProperty(value = "startDate", access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate startDate;

    @JsonProperty(value = "endDate", access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate endDate;

    public static boolean isAllNull(TrackTimeDto trackTimeDto) {
        return Stream.of(trackTimeDto.methodCategory,
                        trackTimeDto.returnType,
                        trackTimeDto.packageName,
                        trackTimeDto.className,
                        trackTimeDto.methodName,
                        trackTimeDto.parameters,
                        trackTimeDto.status,
                        trackTimeDto.createdAt,
                        trackTimeDto.startDate,
                        trackTimeDto.endDate)
                .allMatch(Objects::isNull);
    }
}