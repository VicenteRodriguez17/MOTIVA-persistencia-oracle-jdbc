package dao;

import db.ConexaoBD;
import model.EquipeManutencao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO EQUIPE_MANUTENCAO (NOME_EQUIPE, QTD_INTEGRANTES) VALUES (?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, NOME_EQUIPE, QTD_INTEGRANTES FROM EQUIPE_MANUTENCAO WHERE ID = ?";
    private static final String SQL_BUSCAR_POR_NOME =
            "SELECT ID, NOME_EQUIPE, QTD_INTEGRANTES FROM EQUIPE_MANUTENCAO WHERE NOME_EQUIPE = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, NOME_EQUIPE, QTD_INTEGRANTES FROM EQUIPE_MANUTENCAO ORDER BY ID";
    private static final String SQL_ATUALIZAR =
            "UPDATE EQUIPE_MANUTENCAO SET NOME_EQUIPE = ?, QTD_INTEGRANTES = ? WHERE ID = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM EQUIPE_MANUTENCAO WHERE ID = ?";

    /** Record que representa uma linha da tabela EQUIPE_MANUTENCAO. */
    public record EquipeRecord(int id, String nomeEquipe, int quantidadeIntegrantes) {
    }

    public EquipeManutencaoDAO() {
    }

    public int inserir(EquipeManutencao equipe) {
        int idGerado = -1;
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERT, new String[] { "ID" });
            stmt.setString(1, equipe.getNomeEquipe());
            stmt.setInt(2, equipe.getQuantidadeIntegrantes());
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
                equipe.setId(idGerado);
            }
            System.out.println("Equipe inserida com sucesso! ID: " + idGerado);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir equipe: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return idGerado;
    }

    public EquipeRecord buscarPorId(int id) {
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
            System.err.println("Erro SQL ao buscar equipe por ID: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return null;
    }

    /** Usada pelo GeradorRelatorio para vincular uma intervenção a uma equipe já cadastrada. */
    public EquipeRecord buscarPorNome(String nomeEquipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_NOME);
            stmt.setString(1, nomeEquipe);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearRegistro(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar equipe por nome: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return null;
    }

    public List<EquipeRecord> listarTodas() {
        List<EquipeRecord> lista = new ArrayList<>();
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
            System.err.println("Erro SQL ao listar equipes: " + e.getMessage());
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(EquipeRecord equipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setString(1, equipe.nomeEquipe());
            stmt.setInt(2, equipe.quantidadeIntegrantes());
            stmt.setInt(3, equipe.id());
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar equipe: " + e.getMessage());
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
            // ORA-02292 costuma cair aqui quando existem intervenções vinculadas a esta equipe
            System.err.println("Erro SQL ao deletar equipe: " + e.getMessage());
        } finally {
            fecharRecursos(null, stmt);
        }
        return false;
    }

    private EquipeRecord mapearRegistro(ResultSet rs) throws SQLException {
        return new EquipeRecord(
                rs.getInt("ID"),
                rs.getString("NOME_EQUIPE"),
                rs.getInt("QTD_INTEGRANTES"));
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
