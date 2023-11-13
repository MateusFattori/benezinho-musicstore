package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Music;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MusicRepository implements Repository<Music, Long> {

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

        List<Music> musics = new ArrayList<>();

        var sql = "SELECT * FROM TB_MUSIC";

        Connection conn = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery( sql );
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    var music = buildMusic( rs );
                    musics.add( music );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, st, conn );
        }

        return musics;
    }


    @Override
    public Music findById(Long id) {

        Music music = null;

        var sql = "SELECT * FROM TB_MUSIC WHERE ID_MUSIC = ?";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement( sql );
            ps.setLong( 1, id );
            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    music = buildMusic( rs );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }

        return music;
    }

    /**
     * Mostrando uma nova forma de persistir.
     * <p>
     * Funciona em todos os SGBDR
     *
     * @param music
     * @return
     */
    @Override
    public Music persist(Music music) {

        var sql = "INSERT INTO TB_MUSIC (ID_MUSIC, TITLE, ARTIST, STYLE, DURATION, ORIGINAL_LANGUAGE, EXPLICIT_LIRICS ) VALUES (SQ_MUSIC.nextval, ?,?,?,?,?,?)";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement( sql, new String[]{"ID_MUSIC"} );
            ps.setString( 1, music.getTitle() );
            ps.setLong( 2, music.getArtist().getId() );
            ps.setString( 3, music.getStyle() );
            ps.setString( 4, music.getDuration() );
            ps.setString( 5, music.getOriginalLanguage() );
            ps.setBoolean( 6, music.isExplicitLyrics() );

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                final Long id = rs.getLong( 1 );
                music.setId( id );
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possivel salvar o Music no banco de dados:  " + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }
        return music;
    }

    @Override
    public Music findByName(String texto) {

        Music music = null;

        var sql = "SELECT *  FROM TB_MUSIC WHERE trim(UPPER(TITLE)) = ?";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement( sql );
            ps.setString( 1, texto.toUpperCase().trim() );
            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    music = buildMusic( rs );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }
        return music;
    }

    private Music buildMusic(ResultSet rs) throws SQLException {
        var id = rs.getLong( "ID_MUSIC" );
        var title = rs.getString( "TITLE" );
        var idArtist = rs.getLong( "ARTIST" );
        var style = rs.getString( "STYLE" );
        var duration = rs.getString( "DURATION" );
        var language = rs.getString( "ORIGINAL_LANGUAGE" );
        var explicit = rs.getBoolean( "EXPLICIT_LIRICS" );
        var artista = artistRepository.findById( idArtist );

        var music = new Music( id, title, artista, style, duration, language, explicit );
        return music;
    }
}
