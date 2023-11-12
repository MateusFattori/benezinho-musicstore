package br.com.fiap.domain.dto;

import br.com.fiap.domain.entity.Artist;
import br.com.fiap.domain.entity.Music;
import br.com.fiap.domain.service.ArtistService;
import br.com.fiap.domain.service.MusicService;

import java.util.Objects;

public record MusicDTO(
                        Long id,
                       String title,
                       ArtistDTO artist,
                       String style,
                       String duration,
                       String originalLanguage,
                       boolean explicitLyrics
) {

    static MusicService service = new MusicService();

    static ArtistService artistService = new ArtistService();

    public static Music of(MusicDTO dto){

        if (Objects.isNull(dto)) return null;

        if (Objects.nonNull(dto.id)) return service.findById(dto.id);

        Music m = new Music();
        m.setId(null);


        var music = Objects.nonNull(dto.artist) && (Objects.nonNull(dto.artist.id() )) ?
                artistService.findById(dto.artist.id() )
                : !dto.artist.name().equalsIgnoreCase("")?artistService.persist(ArtistDTO.of(dto.artist)) : null;

                m.setArtist(music);

                m.setTitle(dto.title);
                m.setStyle(dto.style);
                m.setDuration(dto.duration);
                m.setOriginalLanguage(dto.originalLanguage);
                m.setExplicitLyrics(dto.explicitLyrics);

                return m;
    }

    public static MusicDTO of(Music m){
        MusicDTO dto = new MusicDTO(m.getId(), m.getTitle(), ArtistDTO.of(m.getArtist()), m.getStyle(), m.getDuration(), m.getOriginalLanguage(), m.isExplicitLyrics() );
        return dto;
    }
}
