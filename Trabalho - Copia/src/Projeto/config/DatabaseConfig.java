package Projeto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/trabalho";
    private static final String USER = "prog";
    private static final String PASSWORD = "123456";

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados.
     * @return Uma nova conexão JDBC.
     * @throws SQLException Se ocorrer um erro de conexão.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não encontrado.");
            throw new SQLException("Driver não disponível", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Fecha a conexão se ela não for nula.
     * @param connection A conexão a ser fechada.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}