package Projeto.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Projeto.config.DatabaseConfig;
import Projeto.model.Contato;

public class ContatoRepository {

    private Contato mapResultSetContato(ResultSet rs) throws SQLException {
        
        Contato contato = new Contato();

        contato.setId(rs.getInt("id"));
        contato.setContato(rs.getString("contato"));
        contato.setTipo(rs.getString("tipo"));
        contato.setPessoa_id(rs.getInt("pessoa_id"));

        if (rs.wasNull()) {
            contato.setPessoa_id(null);
        } 
        return contato;
    }

    // CREATE
    public Contato create(Contato contato) {

        String sql = "INSERT INTO Contatos (contato, tipo, pessoa_id) values (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // primeiramente a conexão
            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // retorna o id gerado
            ps.setString(1, contato.getContato()); // 1 -> 1°(?)
            ps.setString(2, contato.getTipo()); // 2 -> 2°(?)
            
            if (contato.getPessoa_id() != null) {
                
                ps.setInt(3, contato.getPessoa_id());
            } else {
                
                ps.setNull(3, java.sql.Types.INTEGER); // retorna o valor como null
            }

            int rowsAffected = ps.executeUpdate(); // executa e retorna o n° de linhas afetadas
            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys(); // recupera o id recém criado
                
                if (rs.next()) { // verifca se existe uma próxima linha de resultados
                    contato.setId(rs.getInt(1));
                }
            }
            return contato;
        
        } catch (SQLException e) {
            
            System.err.println("Erro ao cadastrar contato: " + e.getMessage());
            return null;
        
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    // READ (ID)
    public Optional<Contato> findById(int id) {

        String sql = "select * from contatos where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conn = DatabaseConfig.getConnection();
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            rs = ps.executeQuery(); // executa o SELECT

            if (rs.next()) {
                return Optional.of(mapResultSetContato(rs));
            }

        } catch (SQLException e) {

            System.err.println("Erro ao buscar contato por ID: " + e.getMessage());
        } finally {

            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    // READ (TODOS)
    public List<Contato> findAll() {

        String sql = "select * from contatos";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                contatos.add(mapResultSetContato(rs));
            }
        } catch (SQLException e) {
            
            System.err.println("Erro ao listar todos os contatos: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return contatos;
    }

    // READ (PESSOA_ID)
    public List<Contato> findByPessoa_id(int pessoaId) {

        String sql = "select * from contatos where pessoa_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setInt(1, pessoaId);

            rs = ps.executeQuery();

            while(rs.next()) {
                contatos.add(mapResultSetContato(rs));
            }

        } catch(SQLException e) {
            System.err.println("Erro ao listar contatos da pessoa " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return contatos;
    }

    // UPDATE
    public boolean update(Contato contato) {

        String sql = "update contatos set contato = ?, tipo = ?, pessoa_id = ? where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        // ResultSet só é necessário na leitura
        // Uso do ".executeQuery()"

        try {

            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getContato());
            ps.setString(2, contato.getTipo());

            if (contato.getPessoa_id() != null) { // pessoa_id pode ou não ser null
                
                ps.setInt(3, contato.getPessoa_id());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            ps.setInt(4, contato.getId());

            return ps.executeUpdate() > 0; // retornará True ou False verificando se obteve linhas alteradas
        
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contatos " + e.getMessage());
            return false;

        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        
    }

    // DELETE 
    public boolean delete(int id){
        
        String sql = "delete from contatos where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            
            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar contato " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}