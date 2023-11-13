package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Artist;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArtistRepository implements Repository<Artist, Long> {

    private ConnectionFactory factory;

    private static final AtomicReference<ArtistRepository> instance = new AtomicReference<>();

    private ArtistRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static ArtistRepository build() {
        instance.compareAndSet( null, new ArtistRepository() );
        return instance.get();
    }

    @Override
    public List<Artist> findAll() {

        List<Artist> artists = new ArrayList<>();

        var sql = "SELECT * FROM TB_ARTIST ";

        Connection conn = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery( sql );
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    var artist = buildArtist( rs );
                    artists.add( artist );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, st, conn );
        }
        return artists;
    }


    @Override
    public Artist findById(Long id) {

        Artist artist = null;

        var sql = "SELECT * FROM TB_ARTIST WHERE ID_ARTIST = ?";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement( sql );
            ps.setLong( 1, id );
            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    artist = buildArtist( rs );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }
        return artist;
    }

    /**
     * Maneira que vinhamos persistindo.
     * Só funciona no ORACLE
     *
     * @param artist
     * @return
     */
    @Override
    public Artist persist(Artist artist) {

        var sql = "INSERT INTO TB_ARTIST (ID_ARTIST, NM_ARTIST, NATIONALITY) values (SQ_ARTIST.nextval,?,?)";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement( sql, new String[]{"ID_ARTIST"} );
            ps.setString( 1, artist.getName() );
            ps.setString( 2, artist.getNationality() );
            ps.executeUpdate();
            //Pego o ID_ARTISTA
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                artist.setId( rs.getLong( 1 ) );
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível salvar no banco de dados: " + e.getMessage() + "\n" + e.getCause() + "\n" + e.getErrorCode() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }
        return artist;
    }

    @Override
    public Artist findByName(String texto) {

        Artist artist = null;

        var sql = "SELECT * FROM TB_ARTIST WHERE trim(upper(NM_ARTIST)) = ?";

        Connection conn = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement( sql );
            ps.setString( 1, texto.toUpperCase().trim() );
            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    artist = buildArtist( rs );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, conn );
        }
        return artist;
    }

    private static Artist buildArtist(ResultSet rs) throws SQLException {
        var id = rs.getLong( "ID_ARTIST" );
        var name = rs.getString( "NM_ARTIST" );
        var nationality = rs.getString( "NATIONALITY" );
        var artist = new Artist( id, name, nationality );
        return artist;
    }
}
