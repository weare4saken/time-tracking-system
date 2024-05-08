package org.weare4saken.trackingsystem.generator.impl;

import net.datafaker.Faker;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.weare4saken.trackingsystem.annotation.TrackAsyncTime;
import org.weare4saken.trackingsystem.annotation.TrackTime;
import org.weare4saken.trackingsystem.generator.EntityGenerator;
import org.weare4saken.trackingsystem.model.Film;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Component
public class FilmGenerator implements EntityGenerator<Film> {

    private final Faker faker = new Faker();

    @Async
    @TrackAsyncTime
    public CompletableFuture<List<Film>> generateAsCompletableFuture(Long limit) {
        return CompletableFuture.supplyAsync(() -> this.generate(limit));
    }

    @Async
    @TrackAsyncTime
    public Future<List<Film>> generateAsFuture(Long limit) {
        return new AsyncResult<>(this.generate(limit));
    }


    @TrackTime
    @Override
    public List<Film> generate(Long limit) {
        return Stream.generate(() -> new Film(
                        this.faker.random().nextLong(),
                        this.faker.movie().toString(),
                        this.faker.book().genre(),
                        this.faker.name().fullName(),
                        this.faker.random().nextDouble(1, 10)))
                .limit(limit)
                .toList();
    }
}
