package org.weare4saken.trackingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;

@Repository
public interface TrackingTimeStatisticsRepository extends
        JpaRepository<TrackingTimeStatistics, Long>,
        JpaSpecificationExecutor<TrackingTimeStatistics> {
}
