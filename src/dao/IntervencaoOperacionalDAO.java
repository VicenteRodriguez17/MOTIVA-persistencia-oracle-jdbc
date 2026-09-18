package dao;

import db.ConexaoBD;
import model.IntervencaoOperacional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class IntervencaoOperacionalDAO {

    private static final String SQL_INSERT =
            "INSERT INTO INTERVENCAO_OPERACIONAL " +
            "(RESPONSAVEL, DATA_AGENDAMENTO, TIPO, DETALHE, TRECHO_ID, EQUIPE_ID) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, RESPONSAVEL, DATA_AGENDAMENTO, TIPO, DETALHE, TRECHO_ID, EQUIPE_ID " +
            "FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, RESPONSAVEL, DATA_AGENDAMENTO, TIPO, DETALHE, TRECHO_ID, EQUIPE_ID " +
            "FROM INTERVENCAO_OPERACIONAL ORDER BY ID";
    private static final String SQL_ATUALIZAR =
            "UPDATE INTERVENCAO_OPERACIONAL SET RESPONSAVEL = ?, DATA_AGENDAMENTO = ?, TIPO = ?, " +
            "DETALHE = ?, TRECHO_ID = ?, EQUIPE_ID = ? WHERE ID = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";

    /** Record que representa uma linha da tabela INTERVENCAO_OPERACIONAL. */
    public record IntervencaoRecord(int id, String responsavel, String dataAgendamento, String tipo,
                                     String detalhe, int trechoId, Integer equipeId) {
    }

    public IntervencaoOperacionalDAO() {
    }

    public int inserir(IntervencaoOperacional intervencao, int trechoId, Integer equipeId) {
        int idGerado = -1;
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERT, new String[] { "ID" });
            stmt.setString(1, intervencao.getResponsavel());
            stmt.setString(2, intervencao.getDataAgendamento());
            stmt.setString(3, intervencao.getTipo());
            stmt.setString(4, intervencao.getDetalhe());
            stmt.setInt(5, trechoId);
            if (equipeId != null) {
                stmt.setInt(6, equipeId);
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
                intervencao.setId(idGerado);
            }
            System.out.println("Intervenção inserida com sucesso! ID: " + idGerado);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir intervenção: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return idGerado;
    }

    public IntervencaoRecord buscarPorId(int id) {
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
            System.err.println("Erro SQL ao buscar intervenção: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return null;
    }

    public List<IntervencaoRecord> listarTodas() {
        List<IntervencaoRecord> lista = new ArrayList<>();
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
            System.err.println("Erro SQL ao listar intervenções: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(IntervencaoRecord intervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setString(1, intervencao.responsavel());
            stmt.setString(2, intervencao.dataAgendamento());
            stmt.setString(3, intervencao.tipo());
            stmt.setString(4, intervencao.detalhe());
            stmt.setInt(5, intervencao.trechoId());
            if (intervencao.equipeId() != null) {
                stmt.setInt(6, intervencao.equipeId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setInt(7, intervencao.id());
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar intervenção: " + e.getMessage());
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
            System.err.println("Erro SQL ao deletar intervenção: " + e.getMessage());
        } finally {
            fecharRecursos(null, stmt);
        }
        return false;
    }

    private IntervencaoRecord mapearRegistro(ResultSet rs) throws SQLException {
        int equipeIdColuna = rs.getInt("EQUIPE_ID");
        Integer equipeId = rs.wasNull() ? null : equipeIdColuna;
        return new IntervencaoRecord(
                rs.getInt("ID"),
                rs.getString("RESPONSAVEL"),
                rs.getString("DATA_AGENDAMENTO"),
                rs.getString("TIPO"),
                rs.getString("DETALHE"),
                rs.getInt("TRECHO_ID"),
                equipeId);
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
