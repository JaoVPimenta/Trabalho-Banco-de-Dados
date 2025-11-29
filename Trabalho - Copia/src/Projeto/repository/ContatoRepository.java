package Projeto.repository;

import Projeto.config.DatabaseConfig;
import Projeto.model.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContatoRepository {

    private Contato mapResultSetToContato(ResultSet rs) throws SQLException {
        Contato contato = new Contato();
        contato.setId(rs.getInt("id"));
        contato.setContato(rs.getString("contato"));
        contato.setTipo(rs.getString("tipo"));
        contato.setPessoa_id(rs.getInt("pessoa_id"));
        if (rs.wasNull()) { // caso pessoa_id seja NULL no banco
            contato.setPessoa_id(null);
        }
        return contato;
    }

    public Contato create(Contato contato) {
        String sql = "INSERT INTO Contatos (contato, tipo, pessoa_id) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, contato.getContato());
            stmt.setString(2, contato.getTipo());
            if (contato.getPessoa_id() != null) {
                stmt.setInt(3, contato.getPessoa_id());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
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

    public Optional<Contato> findById(int id) {
        String sql = "SELECT * FROM Contatos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToContato(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar contato por ID: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return Optional.empty();
    }

    public List<Contato> findAll() {
        String sql = "SELECT * FROM Contatos";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                contatos.add(mapResultSetToContato(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os contatos: " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return contatos;
    }

    public List<Contato> findByPessoaId(int pessoaId) {
        String sql = "SELECT * FROM Contatos WHERE pessoa_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pessoaId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                contatos.add(mapResultSetToContato(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar contatos da pessoa " + pessoaId + ": " + e.getMessage());
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
        return contatos;
    }

    public boolean update(Contato contato) {
        String sql = "UPDATE Contatos SET contato = ?, tipo = ?, pessoa_id = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, contato.getContato());
            stmt.setString(2, contato.getTipo());
            if (contato.getPessoa_id() != null) {
                stmt.setInt(3, contato.getPessoa_id());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setInt(4, contato.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contato: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Contatos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir contato: " + e.getMessage());
            return false;
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}