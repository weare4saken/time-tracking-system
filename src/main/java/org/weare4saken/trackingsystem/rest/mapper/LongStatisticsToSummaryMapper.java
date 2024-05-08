package org.weare4saken.trackingsystem.rest.mapper;

import org.springframework.stereotype.Component;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeSummary;

import java.util.LongSummaryStatistics;
import java.util.function.Function;

@Component
public class LongStatisticsToSummaryMapper implements Function<LongSummaryStatistics, TrackTimeSummary> {
    @Override
    public TrackTimeSummary apply(LongSummaryStatistics longSummaryStatistics) {
        long count = longSummaryStatistics.getCount();
        long min = longSummaryStatistics.getMin();
        long max = longSummaryStatistics.getMax();
        double average = Math.round(longSummaryStatistics.getAverage() * 100) / 100.0d;

        return new TrackTimeSummary(count, min == Long.MAX_VALUE ? 0 : min, max == Long.MIN_VALUE ? 0 : max, average);
    }
}
