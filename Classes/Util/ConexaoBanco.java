package Classes.Util;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final Dotenv dotenv;
    static {
        Dotenv temp = null;
        try {
            temp = Dotenv.configure().ignoreIfMissing().load();
        } catch (Exception e) {
            System.out.println("Erro crítico ao inicializar Dotenv: " + e.getMessage());
        }
        dotenv = temp;
    }

    private static final String HOST = (dotenv != null) ? dotenv.get("DB_HOST") : null;
    private static final String PORTA = (dotenv != null) ? dotenv.get("DB_PORT") : null;
    private static final String BANCO = (dotenv != null) ? dotenv.get("DB_NAME") : null;
    private static final String USUARIO = (dotenv != null) ? dotenv.get("DB_USER") : null;
    private static final String SENHA = (dotenv != null) ? dotenv.get("DB_PASSWORD") : null;

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + BANCO + "?sslmode=require";

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