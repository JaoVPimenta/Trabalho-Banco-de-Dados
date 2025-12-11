package Projeto.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Projeto.config.DatabaseConfig;
import Projeto.model.Projeto;

public class ProjetoRepository {
    
    private Projeto mapResultSetToProjeto(ResultSet rs) throws SQLException { // Percorre Projeto
        
        Projeto projeto = new Projeto();

        projeto.setId(rs.getInt("id"));
        projeto.setData_inicial(rs.getDate("data_inicial"));
        projeto.setData_final(rs.getDate("data_final"));
        projeto.setNome(rs.getString("nome"));

        return projeto;
    }

    // CREATE
    public Projeto create(Projeto projeto) {

        String sql = "INSERT INTO Projeto (data_inicial, data_final, nome) values (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setDate(1, projeto.getData_inicial());
            ps.setDate(2, projeto.getData_final());
            ps.setString(3,projeto.getNome());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    
                    projeto.setId(rs.getInt(1));
                }
            }
            return projeto;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar projeto " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // READ (ID)
    public Optional<Projeto> findById(int id) {

        String sql = "select * from Projetos where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {

                return Optional.of(mapResultSetToProjeto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar projeto po ID " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    // READ (TODOS) 
    public List<Projeto> findAll() {

        String sql = "select * from Projetos";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Projeto> projetos = new ArrayList<>();

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                projetos.add(mapResultSetToProjeto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar projetos " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return projetos;
    }

    // UPDATE
    public boolean update(Projeto projeto) {

        String sql = "UPDATE Projetos SET data_inicial = ?, data_final = ?, nome = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setDate(1, projeto.getData_inicial());
            ps.setDate(2, projeto.getData_final());
            ps.setString(3, projeto.getNome());
            ps.setInt(4, projeto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar projeto " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // DELETE
    public boolean delete(int id) {
        
        String sql = "DELETE FROM Projetos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        
        } catch (SQLException e) {
            System.err.println("Erro ao deletar projeto " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}