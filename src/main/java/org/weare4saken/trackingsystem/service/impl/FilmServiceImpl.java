package org.weare4saken.trackingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weare4saken.trackingsystem.model.Film;
import org.weare4saken.trackingsystem.repository.FilmRepository;
import org.weare4saken.trackingsystem.service.FilmService;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository repository;

    @Override
    public Film save(Film film) {
        return this.repository.save(film);
    }

    @Override
    public Film findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
