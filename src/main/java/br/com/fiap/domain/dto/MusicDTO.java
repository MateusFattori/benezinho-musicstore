package br.com.fiap.domain.dto;

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

    public static Music of(MusicDTO dto) {

        // É nulo?
        if (Objects.isNull( dto )) return null;

        //Ele informou o id do produto?
        if (Objects.nonNull( dto.id )) return service.findById( dto.id );

        //Se não informou o Id é porque está salvando um novo Music
        Music m = new Music();
        m.setId( null );
        m.setTitle( dto.title );

        //Se existir artist com o ID informado no SGBDR eu recupero todas as informações, mas
        // se não for informado o id do Artist e informou o name eu salvo um novo Artist
        var artist = Objects.nonNull( dto.artist ) && (Objects.nonNull( dto.artist.id() )) ?
                artistService.findById( dto.artist.id() )
                : !dto.artist.name().equalsIgnoreCase( "" ) ? artistService.persist( ArtistDTO.of( dto.artist ) ) : null;

        m.setArtist( artist );
        m.setStyle( dto.style );
        m.setDuration( dto.duration );
        m.setOriginalLanguage( dto.originalLanguage );
        m.setExplicitLyrics( dto.explicitLyrics );


        return m;
    }

    public static MusicDTO of(Music entity) {
        MusicDTO dto = new MusicDTO( entity.getId(), entity.getTitle(), ArtistDTO.of( entity.getArtist() ), entity.getStyle(), entity.getDuration(), entity.getOriginalLanguage(), entity.isExplicitLyrics() );
        return dto;
    }

}
