package Projeto.repository;

import Projeto.config.DatabaseConfig;
import Projeto.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PessoaRepository {

    private Pessoa mapResultSetToPessoa(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(rs.getInt("id"));
        pessoa.setCpf(rs.getString("cpf"));
        pessoa.setData_nascimento(rs.getDate("data_nascimento"));
        pessoa.setNome(rs.getString("nome"));
        pessoa.setSexo(rs.getString("sexo"));
        return pessoa;
    }

    public Pessoa create(Pessoa pessoa) {
        String sql = "INSERT INTO Pessoas (cpf, data_nascimento, nome, sexo) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pessoa.getCpf());
            stmt.setDate(2, pessoa.getData_nascimento());
            stmt.setString(3, pessoa.getNome());
            stmt.setString(4, pessoa.getSexo());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pessoa.setId(rs.getInt(1));
                }
            }
            return pessoa;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar pessoa: " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public Optional<Pessoa> findById(int id) {
        String sql = "SELECT * FROM Pessoas WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPessoa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pessoa por ID: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    public List<Pessoa> findAll() {
        String sql = "SELECT * FROM Pessoas";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pessoa> pessoas = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                pessoas.add(mapResultSetToPessoa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as pessoas: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return pessoas;
    }

    public boolean update(Pessoa pessoa) {
        String sql = "UPDATE Pessoas SET cpf = ?, data_nascimento = ?, nome = ?, sexo = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pessoa.getCpf());
            stmt.setDate(2, pessoa.getData_nascimento());
            stmt.setString(3, pessoa.getNome());
            stmt.setString(4, pessoa.getSexo());
            stmt.setInt(5, pessoa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pessoa: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Pessoas WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir pessoa: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}