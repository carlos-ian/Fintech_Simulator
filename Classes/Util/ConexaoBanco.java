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

    private static final String HOST = "ep-hidden-math-aqg4q940-pooler.c-8.us-east-1.aws.neon.tech";
    private static final String PORTA = "5432";
    private static final String BANCO = "neondb";

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + BANCO + "?sslmode=require";

    private static final String USUARIO = "neondb_owner";
    private static final String SENHA = "npg_SRAZ80OxTBLC";

    public static Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do PostgreSQL não encontrado: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco na nuvem: " + e.getMessage());
            return null;
        }
    }
}