package org.weare4saken.trackingsystem.rest.mapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeDto;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class TrackTimeDtoToSpecificationMapper implements Function<TrackTimeDto, Specification<TrackingTimeStatistics>> {
    private final TrackTimeDtoToEntityMapper toEntityMapper;

    @Override
    public Specification<TrackingTimeStatistics> apply(TrackTimeDto requestDto) {
        TrackingTimeStatistics trackTimeStat = this.toEntityMapper.apply(requestDto);

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            ReflectionUtils.doWithLocalFields(TrackingTimeStatistics.class, field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = ReflectionUtils.getField(field, trackTimeStat);

                if (value != null) {
                    Predicate predicate = null;

                    if (this.isCreatedAtField(field)) {
                        predicate = createCreatedAtPredicate(builder, root, fieldName, value, requestDto);
                    } else {
                        predicate = createRegularPredicate(builder, root, fieldName, value);
                    }

                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                }
            });

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate createCreatedAtPredicate(CriteriaBuilder cb, Root<TrackingTimeStatistics> root, String fieldName, Object value, TrackTimeDto requestDto) {
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        Expression<LocalDate> createdAtColumn = root.get(fieldName).as(LocalDate.class);

        if (startDate != null && endDate != null) {
            return cb.between(createdAtColumn, startDate, endDate);
        } else if (startDate != null) {
            return cb.greaterThanOrEqualTo(createdAtColumn, startDate);
        } else if (endDate != null) {
            return cb.lessThanOrEqualTo(createdAtColumn, endDate);
        } else {
            return cb.equal(createdAtColumn, value);
        }
    }

    private Predicate createRegularPredicate(CriteriaBuilder cb, Root<TrackingTimeStatistics> root, String fieldName, Object value) {
        if (value instanceof String && value.toString().contains("*")) {
            String like = value.toString().replaceAll("\\*", "%");
            return cb.like(root.get(fieldName), like);
        } else {
            return cb.equal(root.get(fieldName), value);
        }
    }

    private boolean isCreatedAtField(Field field) {
        return field.getName().equals("createdAt") ||
                field.getName().equals("created_at") ||
                field.isAnnotationPresent(CreationTimestamp.class) ||
                field.isAnnotationPresent(CreatedDate.class);
    }
}