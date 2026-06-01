package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/sistema_bancario";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver do PostgreSQL não encontrado! Adicione o JAR ao projeto.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
