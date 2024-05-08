package org.weare4saken.trackingsystem.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeDto;
import org.weare4saken.trackingsystem.rest.mapper.LongStatisticsToSummaryMapper;
import org.weare4saken.trackingsystem.rest.mapper.TrackTimeDtoToSpecificationMapper;
import org.weare4saken.trackingsystem.rest.mapper.TrackTimeEntityToDtoMapper;
import org.weare4saken.trackingsystem.service.TrackingTimeStatisticsService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/api/v1/trackingtime")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class TrackTimeRestController {
    public static final class ViewTypes {
        public static final String ALL = "ALL";
        public static final String DATA = "DATA";
        public static final String SUMMARY = "SUMMARY";
    }

    private final TrackingTimeStatisticsService trackingTimeStatisticsService;

    private final TrackTimeDtoToSpecificationMapper toSpecificationMapper;
    private final TrackTimeEntityToDtoMapper toDtoMapper;
    private final LongStatisticsToSummaryMapper statisticsToSummaryMapper;

    @GetMapping({"/stats"})
    public ResponseEntity<?> getTrackTimeStats(@RequestParam(name = "view", defaultValue = "all") String viewType,
                                               @RequestParam(name = "short", defaultValue = "false") boolean shortFormat) {
        List<TrackingTimeStatistics> timeStats = this.trackingTimeStatisticsService.findAll();
        return this.prepareResponse(timeStats, viewType, shortFormat);
    }

    private ResponseEntity<Map<String, Object>> prepareResponse(Collection<? extends TrackingTimeStatistics> data,
                                                                String viewType, boolean shortFormat) {

        Function<TrackingTimeStatistics, TrackTimeDto> toDtoMapper;
        if (shortFormat) {
            toDtoMapper = this.toDtoMapper.toShort();
        } else {
            toDtoMapper = this.toDtoMapper.toNormal();
        }

        List<TrackTimeDto> resultList = data.stream().map(toDtoMapper).toList();
        var statistics = resultList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();
        var summary = this.statisticsToSummaryMapper.apply(statistics);

        Map<String, Object> body = new HashMap<>();

        if (viewType.isBlank() || viewType.equalsIgnoreCase(ViewTypes.ALL)) {
            body.put("result", resultList);
            body.put("summary", summary);
        } else if (viewType.equalsIgnoreCase(ViewTypes.DATA)) {
            body.put("result", resultList);
        } else if (viewType.equalsIgnoreCase(ViewTypes.SUMMARY)) {
            body.put("summary", summary);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
