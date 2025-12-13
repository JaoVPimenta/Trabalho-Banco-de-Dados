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

    // CREATE (Associar Pessoa a Projeto)
    public Integrante create(Integrante integrante) {

        String sql = "insert into Integrantes (cargo, pessoa_id, projeto_id) values (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setString(1, integrante.getCargo());
            ps.setInt(2, integrante.getPessoa_id());
            ps.setInt(3, integrante.getProjeto_id());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return integrante;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erro ao associar pessoa ao projeto " + e.getMessage());
            return null;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // READ (ID)
    public Optional<Integrante> findById(int pessoaId, int projetoId) {

        String sql = "select * from Integrantes where pessoa_id = ? and projeto_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, pessoaId);
            ps.setInt(2, projetoId);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToIntegrante(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar integrante " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    // READ (TUDO)
    public List<Integrante> findAll() {

        String sql = "SELECT I.*, P.nome AS nome_pessoa, Pr.nome AS nome_projeto " +
                     "FROM Integrantes I " +
                     "JOIN Pessoas P ON I.pessoa_id = P.id " +
                     "JOIN Projetos Pr ON I.projeto_id = Pr.id " +
                     "ORDER BY Pr.nome, P.nome";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integrante> integrantes = new ArrayList<>();

        try {
            
            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                
                Integrante i = mapResultSetToIntegrante(rs);

                System.out.println("Projeto: " + rs.getString("nome_projeto") + " | Pessoa: " + rs.getString("nome_pessoa") + " | Cargo: " + i.getCargo());
                integrantes.add(i);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os integrantes " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return integrantes;
    }

    // UPDATE
    public boolean update(Integrante integrante) {

        String sql = "update Integrantes set cargo = ? where pessoa_id = ? and projeto_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setString(1, integrante.getCargo());
            ps.setInt(2, integrante.getPessoa_id());
            ps.setInt(3, integrante.getProjeto_id());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar integrantes " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // DELETE
    public boolean delete(int pessoa_id, int projeto_id) {

        String sql = "delete from Integrantes where pessoa_id = ? and projeto_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, pessoa_id);
            ps.setInt(2, projeto_id);

            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao desassociar pessoa ao projeto " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}