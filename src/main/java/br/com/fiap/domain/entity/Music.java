package br.com.fiap.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Music {
    private Long id;
    private String title;
    private Artist artist;
    private String style;
    private String duration;
    private String originalLanguage;
    private boolean explicitLyrics;
}