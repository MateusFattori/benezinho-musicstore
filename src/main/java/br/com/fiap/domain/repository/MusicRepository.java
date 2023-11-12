package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Music;
import br.com.fiap.infra.ConnectionFactory;
import br.com.fiap.domain.repository.ArtistRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MusicRepository implements Repository<Music, Long>{

    private ArtistRepository artistRepository = ArtistRepository.build();

    private ConnectionFactory factory;

    private static final AtomicReference<MusicRepository> instance = new AtomicReference<>();

    private MusicRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static MusicRepository build() {
        instance.compareAndSet( null, new MusicRepository() );
        return instance.get();
    }
    @Override
    public List<Music> findAll() {

        List<Music> musicas = new ArrayList<>();

        var sql = """
                SELECT *
                FROM TB_MUSIC
                """;

        Connection conn = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery( sql );

            if (rs.isBeforeFirst()){
                while (rs.next()){
                    var id = rs.getLong("ID_MUSIC");
                    var title = rs.getString("TITLE");
                    var style = rs.getString("STYLE");
                    var idArtist = rs.getLong("ARTIST");
                    var artist = artistRepository.findById(idArtist);
                    var duration = rs.getString("DURATION");
                    var originalLanguage = rs.getString("ORIGINAL_LANGUAGE");
                    var explicitLirics = rs.getBoolean("EXPLICIT_LIRICS");
                    musicas.add(new Music(id, title,artist, style, duration, originalLanguage, explicitLirics));
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        }finally {
            fecharObjetos(rs, st, conn);
        }

        return musicas;
    }

    @Override
    public Music findById(Long id) {
        Music music = null;

        var sql = """
                SELECT * 
                FROM TB_MUSIC
                WHERE ID_MUSIC = ?
                """;

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    var idMusic = rs.getLong("ID_MUSIC");
                    var title = rs.getString("TITLE");
                    var style = rs.getString("STYLE");
                    var idArtist = rs.getLong("ARTIST");
                    var artist = artistRepository.findById(idArtist);
                    var duration = rs.getString("DURATION");
                    var originalLanguage = rs.getString("ORIGINAL_LANGUAGE");
                    var explicitLirics = rs.getBoolean("EXPLICIT_LIRICS");
                    music = new Music(idMusic, title,artist, style, duration, originalLanguage, explicitLirics);
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        }fecharObjetos(rs, ps, conn);

        return music;
    }

    @Override
    public Music persist(Music music) {
        var sql = """
                INSERT INTO TB_MUSIC
                (ID_MUSIC, TITLE, STYLE, DURATION, ORIGINAL_LANGUAGE, EXPLICIT_LIRICS, ARTIST )
                VALUES
                (SQ_MUSIC.nextval, ?,?,?,?,?,?,?)
                """;

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql, new String[]{"ID_MUSIC"});
            ps.setString(1, music.getTitle());
            ps.setString(2, music.getStyle());
            ps.setString(3, music.getDuration());
            ps.setString(4, music.getOriginalLanguage());
            ps.setBoolean(5, music.isExplicitLyrics());
            ps.setLong(6, music.getArtist().getId());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()){
                final Long id = rs.getLong(1);
                music.setId(id);
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possivel salvar o Movie no banco de dados:  " + e.getMessage() );
        }fecharObjetos(rs, ps, conn);


        return music;
    }

    @Override
    public Music findByName(String texto) {

        Music music = null;

        var sql = """
                SELECT *
                FROM TB_MUSIC
                WHERE trim(upper(NM_ARTIST)) = ?
                """;

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, texto.toUpperCase().trim());
            rs = ps.executeQuery();
            if (rs.next()){
                var idMusic = rs.getLong("ID_MUSIC");
                var title = rs.getString("TITLE");
                var style = rs.getString("STYLE");
                var idArtist = rs.getLong("ARTIST");
                var artist = artistRepository.findById(idArtist);
                var duration = rs.getString("DURATION");
                var originalLanguage = rs.getString("ORIGINAL_LANGUAGE");
                var explicitLirics = rs.getBoolean("EXPLICIT_LIRICS");
                music = new Music(idMusic, title,artist, style, duration, originalLanguage, explicitLirics);
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        }fecharObjetos(rs, ps, conn);

        return music;
    }

    @Override
    public void fecharObjetos(ResultSet rs, Statement st, Connection con) {
        Repository.super.fecharObjetos(rs, st, con);
    }
}
