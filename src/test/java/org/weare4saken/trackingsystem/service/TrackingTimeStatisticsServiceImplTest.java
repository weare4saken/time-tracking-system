package org.weare4saken.trackingsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.repository.TrackingTimeStatisticsRepository;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackingTimeStatisticsServiceImplTest {

    @Mock
    TrackingTimeStatisticsRepository repository;

    @InjectMocks
    TrackingTimeStatisticsService service;

    @Test
    void saveStat_shouldCallRepository() {
        TrackingTimeStatistics trackTimeStat = mock(TrackingTimeStatistics.class);
        when(this.repository.save(trackTimeStat)).thenReturn(trackTimeStat);

        TrackingTimeStatistics saved = this.service.save(trackTimeStat);

        verify(this.repository).save(trackTimeStat);
        assertEquals(trackTimeStat, saved);
    }

    @Test
    void saveAsyncStat_shouldCallRepository() throws Exception {
        TrackingTimeStatistics trackTimeStat = mock(TrackingTimeStatistics.class);
        when(this.repository.save(trackTimeStat)).thenReturn(trackTimeStat);

        CompletableFuture<TrackingTimeStatistics> completableFuture = this.service.saveAsync(trackTimeStat);

        verify(this.repository).save(trackTimeStat);
        assertEquals(trackTimeStat, completableFuture.get());
    }

    @Test
    void findAll_shouldCallRepository() {
        this.service.findAll();

        verify(this.repository).findAll();
    }

    @Test
    void findAllWithSpecification_shouldCallRepository() {
        Specification<TrackingTimeStatistics> specification = Specification.anyOf();
        this.service.findAll(specification);

        verify(this.repository).findAll(specification);
    }

    @Test
    void deleteAll_shouldCallRepository() {
        this.service.deleteAll();

        verify(this.repository).deleteAll();
    }
}
