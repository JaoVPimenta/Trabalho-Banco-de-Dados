package Projeto.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Projeto.config.DatabaseConfig;
import Projeto.model.Pessoa;

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

    // CREATE
    public Pessoa create(Pessoa pessoa) {

        String sql = "INSERT INTO Pessoas (cpf, data_nascimento, nome, sexo) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, pessoa.getCpf());
            ps.setDate(2, pessoa.getData_nascimento());
            ps.setString(3, pessoa.getNome());
            ps.setString(4, pessoa.getSexo());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    
                    pessoa.setId(rs.getInt(1));
                }
            }
            return pessoa;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar pessoa " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // READ (ID)
    public Optional<Pessoa> findById(int id) {

        String sql = "select * from Pessoas where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPessoa(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pessoa por ID " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    // READ (TUDO)
    public List<Pessoa> findAll() {

        String sql = "select * from Pessoas";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Pessoa> pessoas = new ArrayList<>();

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                pessoas.add(mapResultSetToPessoa(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pessoas " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return pessoas;
    }

    // UPDATE
    public boolean update(Pessoa pessoa) {

        String sql = "UPDATE Pessoas SET cpf = ?, data_nascimento = ?, nome = ?, sexo = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setString(1, pessoa.getCpf());
            ps.setDate(2, pessoa.getData_nascimento());
            ps.setString(3, pessoa.getNome());
            ps.setString(4, pessoa.getSexo());
            ps.setInt(5, pessoa.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pessoa " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // DELETE
    public boolean delete(int id) {

        String sql = "delete from Pessoas where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pessoa " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}