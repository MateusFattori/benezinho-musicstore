package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Music;
import br.com.fiap.domain.repository.MusicRepository;

import java.util.List;
import java.util.Objects;

public class MusicService implements Service<Music, Long> {

    MusicRepository repo = MusicRepository.build();

    @Override
    public List<Music> findAll() {
        return repo.findAll();
    }

    @Override
    public Music findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Music persist(Music music) {
        var m = repo.findByName(music.getTitle());
        if (Objects.nonNull(m)) {
            System.err.println( "Já existe Filme cadastrado com o mesmo Título: " + m.getTitle() );
            return m;
        }
        return null;
    }
}
