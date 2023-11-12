package br.com.fiap.domain.dto;

import br.com.fiap.domain.entity.Artist;
import br.com.fiap.domain.service.ArtistService;

import java.util.Objects;

public record ArtistDTO(Long id, String name,  String nationality ) {

    private static ArtistService service = new ArtistService();

    public static Artist of(ArtistDTO dto){

        if (Objects.isNull( dto )) return null;

        if (Objects.nonNull( dto.id )) return service.findById( dto.id) ;

        Artist a = new Artist();

        a.setId( null );
        a.setName( dto.name );
        a.setNationality( dto.nationality );

        return a;
    }

    public static ArtistDTO of(Artist a){
        return new ArtistDTO( a.getId(), a.getName(), a.getNationality());
    }

}
