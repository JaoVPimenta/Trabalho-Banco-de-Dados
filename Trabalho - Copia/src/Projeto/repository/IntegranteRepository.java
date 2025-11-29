package Projeto.repository;

import Projeto.config.DatabaseConfig;
import Projeto.model.Integrante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntegranteRepository {

    private Integrante mapResultSetToIntegrante(ResultSet rs) throws SQLException {
        Integrante integrante = new Integrante();
        integrante.setCargo(rs.getString("cargo"));
        integrante.setPessoa_id(rs.getInt("pessoa_id"));
        integrante.setProjeto_id(rs.getInt("projeto_id"));
        return integrante;
    }

    public Integrante create(Integrante integrante) {
        String sql = "INSERT INTO Integrantes (cargo, pessoa_id, projeto_id) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, integrante.getCargo());
            stmt.setInt(2, integrante.getPessoa_id());
            stmt.setInt(3, integrante.getProjeto_id());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return integrante;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao associar pessoa " + integrante.getPessoa_id() + " ao projeto " + integrante.getProjeto_id() + ": " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public Optional<Integrante> findById(int pessoaId, int projetoId) {
        String sql = "SELECT * FROM Integrantes WHERE pessoa_id = ? AND projeto_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pessoaId);
            stmt.setInt(2, projetoId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToIntegrante(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar integrante: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    public List<Integrante> findAll() {
        String sql = "SELECT I.*, P.nome AS nome_pessoa, Pr.nome AS nome_projeto " + "FROM Integrantes I " + "JOIN Pessoas P ON I.pessoa_id = P.id " + "JOIN Projetos Pr ON I.projeto_id = Pr.id " + "ORDER BY Pr.nome, P.nome";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integrante> integrantes = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Integrante integrante = mapResultSetToIntegrante(rs);
                System.out.println("  > Projeto: " + rs.getString("nome_projeto") + " | Pessoa: " + rs.getString("nome_pessoa") + " | Cargo: " + integrante.getCargo());
                integrantes.add(integrante);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os integrantes: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return integrantes;
    }

    // UPDATE
    public boolean update(Integrante integrante) {
        String sql = "UPDATE Integrantes SET cargo = ? WHERE pessoa_id = ? AND projeto_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, integrante.getCargo());
            stmt.setInt(2, integrante.getPessoa_id());
            stmt.setInt(3, integrante.getProjeto_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar integrante: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // DELETE (Excluir associação)
    public boolean delete(int pessoaId, int projetoId) {
        String sql = "DELETE FROM Integrantes WHERE pessoa_id = ? AND projeto_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pessoaId);
            stmt.setInt(2, projetoId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao desassociar pessoa do projeto: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}