package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Artist;
import br.com.fiap.domain.repository.ArtistRepository;

import java.util.List;
import java.util.Objects;

public class ArtistService implements Service<Artist, Long> {

    ArtistRepository repo = ArtistRepository.build();

    @Override
    public List<Artist> findAll() {
        return repo.findAll();
    }

    @Override
    public Artist findById(Long id) {
        return repo.findById( id );
    }

    @Override
    public Artist persist(Artist artist) {
        //Não pode ter Genero com mesmo nome
        var g = repo.findByName( artist.getName() );
        if (Objects.nonNull( g )){
            System.err.println("Já existe genero cadastrado com o mesmo nome: " + g.getName());
            return g;
        }
        return repo.persist( artist );
    }
}
