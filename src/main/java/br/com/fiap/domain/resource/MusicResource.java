package br.com.fiap.domain.resource;

import br.com.fiap.domain.entity.Music;
import jakarta.ws.rs.core.Response;

public class MusicResource implements Resource<Music, Long> {
    @Override
    public Response findAll() {
        return null;
    }

    @Override
    public Response findById(Long id) {
        return null;
    }

    @Override
    public Response persist(Music music) {
        return null;
    }
}
