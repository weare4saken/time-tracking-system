package org.weare4saken.trackingsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.weare4saken.trackingsystem.model.Film;
import org.weare4saken.trackingsystem.repository.FilmRepository;
import org.weare4saken.trackingsystem.service.impl.FilmServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceImplTest {

    @Mock
    FilmRepository repository;

    @InjectMocks
    FilmServiceImpl service;

    @Test
    void saveFIlm_shouldCallRepository() {
        Film film = mock(Film.class);
        when(this.repository.save(film)).thenReturn(film);

        Film saved = this.service.save(film);

        verify(this.repository).save(film);
        assertEquals(film, saved);
    }

    @Test
    void findFilmById_shouldCallRepository() {
        Film film = mock(Film.class);
        when(this.repository.findById(1L)).thenReturn(Optional.of(film));

        Film saved = this.service.findById(1L);

        verify(this.repository).findById(1L);
        assertEquals(film, saved);
    }
}
