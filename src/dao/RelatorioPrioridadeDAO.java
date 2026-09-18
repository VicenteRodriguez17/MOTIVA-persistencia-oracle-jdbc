package dao;

import db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPrioridadeDAO {

    private static final String SQL_INSERT =
            "INSERT INTO RELATORIO_PRIORIDADE (QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO " +
            "FROM RELATORIO_PRIORIDADE WHERE ID = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO " +
            "FROM RELATORIO_PRIORIDADE ORDER BY DATA_GERACAO DESC";
    private static final String SQL_ATUALIZAR =
            "UPDATE RELATORIO_PRIORIDADE SET QT_URGENTE = ?, QT_CRITICO = ?, QT_ATENCAO = ?, " +
            "QT_NORMAL = ?, RESUMO = ? WHERE ID = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM RELATORIO_PRIORIDADE WHERE ID = ?";

    /** Record que representa uma linha da tabela RELATORIO_PRIORIDADE. */
    public record RelatorioRecord(int id, Timestamp dataGeracao, int qtUrgente, int qtCritico,
                                   int qtAtencao, int qtNormal, String resumo) {
    }

    public RelatorioPrioridadeDAO() {
    }

    public int salvarRelatorio(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo) {
        return inserir(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);
    }

    public int inserir(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo) {
        int idGerado = -1;
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERT, new String[] { "ID" });
            stmt.setInt(1, qtUrgente);
            stmt.setInt(2, qtCritico);
            stmt.setInt(3, qtAtencao);
            stmt.setInt(4, qtNormal);
            stmt.setString(5, resumo);
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
            System.out.println("Histórico de relatório salvo com sucesso! ID: " + idGerado);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao salvar relatório: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return idGerado;
    }

    public RelatorioRecord buscarPorId(int id) {
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
            System.err.println("Erro SQL ao buscar relatório: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return null;
    }

    public List<RelatorioRecord> listarTodas() {
        List<RelatorioRecord> lista = new ArrayList<>();
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
            System.err.println("Erro SQL ao listar relatórios: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(RelatorioRecord relatorio) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setInt(6, relatorio.id());
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar relatório: " + e.getMessage());
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
            System.err.println("Erro SQL ao deletar relatório: " + e.getMessage());
        } finally {
            fecharRecursos(null, stmt);
        }
        return false;
    }

    private RelatorioRecord mapearRegistro(ResultSet rs) throws SQLException {
        return new RelatorioRecord(
                rs.getInt("ID"),
                rs.getTimestamp("DATA_GERACAO"),
                rs.getInt("QT_URGENTE"),
                rs.getInt("QT_CRITICO"),
                rs.getInt("QT_ATENCAO"),
                rs.getInt("QT_NORMAL"),
                rs.getString("RESUMO"));
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
