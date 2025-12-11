package Projeto.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    // "final" -> contante (o valor não será alterado)
    private static final String URL = "jdbc:mysql://localhost:3306/trabalho";
    private static final String USER = "prog";
    private static final String PASSWORD = "123456";

    // estabelece e retorna uma nova conexão com o banco de dados.
    public static Connection getConnection() throws SQLException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {

            System.err.println("Driver JDBC do MySQL não encontrado.");
            // razão do erro
            throw new SQLException("Driver não disponível", e); // somente lançado por conta do "throws SQLException"
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // fecha a conexão se não for nula
    public static void closeConnection(Connection conn) {
        
        if (conn != null) {

            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}