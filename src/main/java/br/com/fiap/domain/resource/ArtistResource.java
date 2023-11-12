package br.com.fiap.domain.resource;

import br.com.fiap.domain.entity.Artist;
import jakarta.ws.rs.core.Response;

public class ArtistResource implements Resource<Artist, Long>{
    @Override
    public Response findAll() {
        return null;
    }

    @Override
    public Response findById(Long id) {
        return null;
    }

    @Override
    public Response persist(Artist artist) {
        return null;
    }
}
