package org.weare4saken.trackingsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.weare4saken.trackingsystem.model.Film;

@NoRepositoryBean
public interface FilmRepository extends CrudRepository<Film, Long> {
}
