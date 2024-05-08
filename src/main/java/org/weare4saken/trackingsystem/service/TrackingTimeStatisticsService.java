package org.weare4saken.trackingsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.repository.TrackingTimeStatisticsRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TrackingTimeStatisticsService {

    private final TrackingTimeStatisticsRepository repository;

    @Transactional
    public TrackingTimeStatistics save(TrackingTimeStatistics stats) {
        return this.repository.save(stats);
    }

    @Async
    @Transactional
    public CompletableFuture<TrackingTimeStatistics> saveAsync(TrackingTimeStatistics stats) {
        return CompletableFuture.completedFuture(this.save(stats));
    }

    public List<TrackingTimeStatistics> findAll() {
        return this.repository.findAll();
    }

    public List<TrackingTimeStatistics> findAll(Specification<TrackingTimeStatistics> specification) {
        return this.repository.findAll(specification);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }
}
