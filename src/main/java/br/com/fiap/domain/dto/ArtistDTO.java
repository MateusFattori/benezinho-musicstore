package br.com.fiap.domain.dto;

import br.com.fiap.domain.entity.Artist;
import br.com.fiap.domain.service.ArtistService;

import java.util.Objects;

public record ArtistDTO(Long id, String name, String nationality) {

    private static ArtistService service = new ArtistService();

    public static Artist of(ArtistDTO dto) {

        // É nulo?
        if (Objects.isNull( dto )) return null;

        //Ele informou o id?
        if (Objects.nonNull( dto.id )) return service.findById( dto.id );

        //Se não informou o Id é porque está salvando um novo Artist
        Artist artist = new Artist();
        artist.setId( null );
        artist.setName( dto.name );
        artist.setNationality( dto.nationality );

        return artist;
    }


    public static ArtistDTO of(Artist entity) {
        return new ArtistDTO( entity.getId(), entity.getName(), entity.getNationality() );
    }

}
