package org.weare4saken.trackingsystem.service;

import org.weare4saken.trackingsystem.model.Film;

public interface FilmService {
    Film save(Film employee);

    Film findById(Long id);
}
