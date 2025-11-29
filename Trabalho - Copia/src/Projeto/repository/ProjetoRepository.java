package Projeto.repository;

import Projeto.config.DatabaseConfig;
import Projeto.model.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjetoRepository {

    private Projeto mapResultSetToProjeto(ResultSet rs) throws SQLException {
        Projeto projeto = new Projeto();
        projeto.setId(rs.getInt("id"));
        projeto.setData_inicial(rs.getDate("data_inicial"));
        projeto.setData_final(rs.getDate("data_final"));
        projeto.setNome(rs.getString("nome"));
        return projeto;
    }

    public Projeto create(Projeto projeto) {
        String sql = "INSERT INTO Projetos (data_inicial, data_final, nome) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, projeto.getData_inicial());
            stmt.setDate(2, projeto.getData_final());
            stmt.setString(3, projeto.getNome());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    projeto.setId(rs.getInt(1));
                }
            }
            return projeto;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar projeto: " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public Optional<Projeto> findById(int id) {
        String sql = "SELECT * FROM Projetos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProjeto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projeto por ID: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    public List<Projeto> findAll() {
        String sql = "SELECT * FROM Projetos";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Projeto> projetos = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                projetos.add(mapResultSetToProjeto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os projetos: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return projetos;
    }

    public boolean update(Projeto projeto) {
        String sql = "UPDATE Projetos SET data_inicial = ?, data_final = ?, nome = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, projeto.getData_inicial());
            stmt.setDate(2, projeto.getData_final());
            stmt.setString(3, projeto.getNome());
            stmt.setInt(4, projeto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar projeto: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Projetos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir projeto: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}