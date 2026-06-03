package Classes.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public static void main(String[] args) {
        try {
            Connection conn = conectar();
            if (conn != null) {
                System.out.println("CONEXÃO COM O POSTGRESQL REALIZADA COM SUCESSO!");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("FALHA NA CONEXÃO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/sistema_bancario";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver não encontrado na pasta lib.", e);
        }
    }
}