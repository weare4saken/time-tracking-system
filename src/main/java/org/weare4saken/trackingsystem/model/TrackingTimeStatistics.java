package org.weare4saken.trackingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracking_time_statistics")
public class TrackingTimeStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "method_сategory", nullable = false)
    private String methodCategory;

    @Column(name = "return_type", nullable = false)
    private String returnType;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "method_name", nullable = false)
    private String methodName;

    @Column(name = "parameters", nullable = false)
    private String parameters;

    @Column(name = "execution_time", nullable = false)
    private long executionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_status", nullable = false)
    private TrackTimeMethodStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
