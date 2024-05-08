package org.weare4saken.trackingsystem.repository.impl;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import org.weare4saken.trackingsystem.model.Film;
import org.weare4saken.trackingsystem.repository.FilmRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@Repository
@NonNullApi
public class FilmRepositoryImpl implements FilmRepository {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();

    @Override
    public <S extends Film> S save(S entity) {
        Objects.requireNonNull(entity);
        this.films.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends Film> Iterable<S> saveAll(Iterable<S> entities) {
        Objects.requireNonNull(entities);
        entities.forEach(this::save);
        return entities;
    }

    @Override
    public Optional<Film> findById(Long aLong) {
        Objects.requireNonNull(aLong);
        return Optional.ofNullable(this.films.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        Objects.requireNonNull(aLong);
        return this.films.containsKey(aLong);
    }

    @Override
    public Iterable<Film> findAll() {
        return this.films.values();
    }

    @Override
    public Iterable<Film> findAllById(Iterable<Long> longs) {
        Objects.requireNonNull(longs);

        return this.films.values()
                .stream()
                .filter(employee -> StreamSupport.stream(longs.spliterator(), false)
                        .anyMatch(l -> employee.getId().equals(l)))
                .toList();
    }

    @Override
    public long count() {
        return this.films.size();
    }

    @Override
    public void deleteById(Long aLong) {
        Objects.requireNonNull(aLong);
        this.films.remove(aLong);
    }

    @Override
    public void delete(Film entity) {
        Objects.requireNonNull(entity);
        this.deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        Objects.requireNonNull(longs);
        longs.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Film> entities) {
        Objects.requireNonNull(entities);
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        this.films.clear();
    }
}
