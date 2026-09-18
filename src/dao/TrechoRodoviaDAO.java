package dao;

import db.ConexaoBD;
import model.TrechoRodovia;
import model.TrechoSeco;
import model.TrechoUmido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrechoRodoviaDAO {

    private static final String SQL_INSERT =
            "INSERT INTO TRECHO_RODOVIA (KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO) VALUES (?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO FROM TRECHO_RODOVIA WHERE ID = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO FROM TRECHO_RODOVIA ORDER BY ID";
    private static final String SQL_ATUALIZAR =
            "UPDATE TRECHO_RODOVIA SET KM_INICIAL = ?, KM_FINAL = ?, ALTURA_VEGETACAO = ?, TIPO = ? WHERE ID = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM TRECHO_RODOVIA WHERE ID = ?";

    /** Record que representa uma linha da tabela TRECHO_RODOVIA. */
    public record TrechoRecord(int id, int kmInicial, int kmFinal, double alturaVegetacao, String tipo) {
    }

    public TrechoRodoviaDAO() {
    }

    public int inserir(TrechoRodovia trecho) {
        int idGerado = -1;
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERT, new String[] { "ID" });
            stmt.setInt(1, trecho.getKmInicial());
            stmt.setInt(2, trecho.getKmFinal());
            stmt.setDouble(3, trecho.getAlturaVegetacao());
            stmt.setString(4, trecho.getTipo());
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
                trecho.setId(idGerado);
            }
            System.out.println("Trecho inserido com sucesso! ID: " + idGerado);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir trecho: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return idGerado;
    }

    public TrechoRecord buscarPorId(int id) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearRegistro(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar trecho: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return null;
    }

    public List<TrechoRecord> listarTodas() {
        List<TrechoRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_LISTAR_TODAS);
            while (rs.next()) {
                lista.add(mapearRegistro(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar trechos: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(TrechoRecord trecho) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setInt(1, trecho.kmInicial());
            stmt.setInt(2, trecho.kmFinal());
            stmt.setDouble(3, trecho.alturaVegetacao());
            stmt.setString(4, trecho.tipo());
            stmt.setInt(5, trecho.id());
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar trecho: " + e.getMessage());
        } finally {
            fecharRecursos(null, stmt);
        }
        return false;
    }

    public boolean deletar(int id) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_DELETAR);
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            // ORA-02292 costuma cair aqui quando existem intervenções vinculadas a este trecho
            System.err.println("Erro SQL ao deletar trecho: " + e.getMessage());
        } finally {
            fecharRecursos(null, stmt);
        }
        return false;
    }

    /** Reconstrói o objeto polimórfico (TrechoUmido/TrechoSeco) a partir do registro do banco. */
    public TrechoRodovia paraObjeto(TrechoRecord registro) {
        TrechoRodovia trecho;
        if ("TrechoUmido".equals(registro.tipo())) {
            trecho = new TrechoUmido(registro.kmInicial(), registro.kmFinal(), registro.alturaVegetacao());
        } else {
            trecho = new TrechoSeco(registro.kmInicial(), registro.kmFinal(), registro.alturaVegetacao());
        }
        trecho.setId(registro.id());
        return trecho;
    }

    private TrechoRecord mapearRegistro(ResultSet rs) throws SQLException {
        return new TrechoRecord(
                rs.getInt("ID"),
                rs.getInt("KM_INICIAL"),
                rs.getInt("KM_FINAL"),
                rs.getDouble("ALTURA_VEGETACAO"),
                rs.getString("TIPO"));
    }

    private void fecharRecursos(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
