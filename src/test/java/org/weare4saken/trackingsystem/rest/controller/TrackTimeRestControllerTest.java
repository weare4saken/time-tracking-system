package org.weare4saken.trackingsystem.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.weare4saken.trackingsystem.model.TrackTimeMethodStatus;
import org.weare4saken.trackingsystem.model.TrackingTimeStatistics;
import org.weare4saken.trackingsystem.repository.TrackingTimeStatisticsRepository;
import org.weare4saken.trackingsystem.rest.dto.TrackTimeDto;
import org.weare4saken.trackingsystem.rest.mapper.LongStatisticsToSummaryMapper;
import org.weare4saken.trackingsystem.rest.mapper.TrackTimeDtoToEntityMapper;
import org.weare4saken.trackingsystem.rest.mapper.TrackTimeDtoToSpecificationMapper;
import org.weare4saken.trackingsystem.rest.mapper.TrackTimeEntityToDtoMapper;
import org.weare4saken.trackingsystem.service.TrackingTimeStatisticsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackTimeRestController.class)
@ActiveProfiles("test")
public class TrackTimeRestControllerTest {

    @MockBean
    TrackingTimeStatisticsRepository repository;

    @SpyBean
    TrackingTimeStatisticsService service;

    @SpyBean
    TrackTimeDtoToEntityMapper trackTimeDtoToEntityMapper;

    @SpyBean
    TrackTimeDtoToSpecificationMapper toSpecificationMapper;

    @SpyBean
    TrackTimeEntityToDtoMapper toDtoMapper;

    @SpyBean
    LongStatisticsToSummaryMapper statisticsToSummaryMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    List<TrackingTimeStatistics> trackTimeStats;

    @BeforeEach
    void setUp() {
        this.trackTimeStats = List.of(
                TrackingTimeStatistics.builder()
                        .id(1L)
                        .methodCategory("async")
                        .packageName("org.weare4saken.trackingsystem.service")
                        .className("MovieService")
                        .methodName("addMovie")
                        .executionTime(1983L)
                        .parameters("Object")
                        .status(TrackTimeMethodStatus.COMPLETED)
                        .createdAt(LocalDateTime.now().minusDays(3))
                        .build(),
                TrackingTimeStatistics.builder()
                        .id(2L)
                        .methodCategory("sync")
                        .packageName("org.weare4saken.trackingsystem.service")
                        .className("MusicService")
                        .methodName("addAlbum")
                        .executionTime(456L)
                        .parameters("Object")
                        .status(TrackTimeMethodStatus.EXCEPTION)
                        .createdAt(LocalDateTime.now().minusDays(4))
                        .build(),
                TrackingTimeStatistics.builder()
                        .id(3L)
                        .methodCategory("sync")
                        .packageName("org.weare4saken.trackingsystem.service")
                        .className("VideogameService")
                        .methodName("getVideogame")
                        .executionTime(612L)
                        .parameters(null)
                        .status(TrackTimeMethodStatus.COMPLETED)
                        .createdAt(LocalDateTime.now().minusDays(5))
                        .build()
        );
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats :: query params are default")
    void handleGetStats_whenQueryParamsAreDefault_returnsAllDataWithSummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toNormal()).toList();
        var statistics = dtoList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();

        Map<String, Object> resultBody = Map.of(
                "result", dtoList,
                "summary", this.statisticsToSummaryMapper.apply(statistics)
        );
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=all")
    void handleGetStats_whenViewTypeIsAll_returnsAllDataWithSummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toNormal()).toList();
        var statistics = dtoList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();

        Map<String, Object> resultBody = Map.of(
                "result", dtoList,
                "summary", this.statisticsToSummaryMapper.apply(statistics)
        );
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "all"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=data")
    void handleGetStats_whenViewTypeIsData_returnsOnlyDataWithNoSummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toNormal()).toList();

        Map<String, Object> resultBody = Map.of("result", dtoList);
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "data"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=summary")
    void handleGetStats_whenViewTypeIsSummary_returnsOnlySummaryWithNoData() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toNormal()).toList();
        var statistics = dtoList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();

        Map<String, Object> resultBody = Map.of("summary", this.statisticsToSummaryMapper.apply(statistics));
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "summary"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=all&short=true")
    void handleGetStats_whenViewTypeIsAll_shortFormatIsTrue_returnsAllDataInShortFormatWithSummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toShort()).toList();
        var statistics = dtoList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();

        Map<String, Object> resultBody = Map.of(
                "result", dtoList,
                "summary", this.statisticsToSummaryMapper.apply(statistics)
        );
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "all")
                        .queryParam("short", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=data&short=true")
    void handleGetStats_whenViewTypeIsData_shortFormatIsTrue_returnsOnlyDataInShortFormatWithNoSummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toShort()).toList();

        Map<String, Object> resultBody = Map.of("result", dtoList);
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "data")
                        .queryParam("short", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/trackingtime/stats?view=summary&short=true")
    void handleGetStats_whenViewTypeIsSummary_shortFormatIsTrue_returnsOnlySummary() throws Exception {
        when(this.repository.findAll()).thenReturn(this.trackTimeStats);

        List<TrackTimeDto> dtoList = this.trackTimeStats.stream().map(this.toDtoMapper.toShort()).toList();

        var statistics = dtoList.stream().mapToLong(TrackTimeDto::getExecutionTime).summaryStatistics();

        Map<String, Object> resultBody = Map.of("summary", this.statisticsToSummaryMapper.apply(statistics));
        String jsonContent = this.objectMapper.writeValueAsString(resultBody);

        this.mockMvc.perform(get("/api/v1/trackingtime/stats").contentType(MediaType.APPLICATION_JSON)
                        .queryParam("view", "summary")
                        .queryParam("short", "true"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonContent, true)
                );

        verify(this.repository).findAll();
    }
}
