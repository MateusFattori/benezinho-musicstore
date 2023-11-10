package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Music;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MusicRepository implements Repository<Music, Long>{

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
            st = conn.createStatement();
            rs = st.executeQuery( sql );

            if (rs.isBeforeFirst()){
                while (rs.next()){
                    var id = rs.getLong("ID_MUSIC");
                    var title = rs.getString("TITLE");
                    var style = rs.getString("STYLE");
                    var artist = rs.getLong("ID_ARTIST");
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a consulta ao banco de dados: " + e.getMessage() );
        }

        return null;
    }

    @Override
    public Music findById(Long id) {
        return null;
    }

    @Override
    public Music persist(Music music) {
        return null;
    }

    @Override
    public Music findByName(String texto) {
        return null;
    }

    @Override
    public void fecharObjetos(ResultSet rs, Statement st, Connection con) {
        Repository.super.fecharObjetos(rs, st, con);
    }
}
