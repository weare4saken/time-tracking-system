package org.weare4saken.trackingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private Long id;
    private String title;
    private String genre;
    private String director;
    private Double rating;
}
