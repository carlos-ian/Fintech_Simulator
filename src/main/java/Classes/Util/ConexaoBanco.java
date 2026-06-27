package Classes.Util;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /**
     * Classe responsável por gerir a conexão da aplicação
     * com o banco de dados PostgreSQL.
     *
     * <p>As informações de conexão são carregadas a partir de
     * variáveis de ambiente definidas num arquivo <code>.env</code>,
     * utilizando a biblioteca Dotenv.</p>
     *
     * <p>Esta classe centraliza a criação de conexões com o banco,
     * permitindo que os repositórios da aplicação realizem operações
     * de persistência de forma padronizada.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

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

        /**
         * Método utilizado para testar a conexão com o banco.
         *
         * <p>Ao executar esta classe diretamente, será realizada
         * uma tentativa de conexão com o PostgreSQL. Em caso de
         * sucesso, uma mensagem será exibida no console.</p>
         *
         * @param args Argumentos de linha de comando.
         */

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

        /**
         * Cria e retorna uma conexão com o banco de dados.
         *
         * <p>O método realiza o carregamento do driver JDBC do
         * PostgreSQL e utiliza os dados obtidos do arquivo
         * <code>.env</code> para estabelecer a conexão.</p>
         *
         * <p>Caso ocorra algum erro durante o processo,
         * o método retorna {@code null}.</p>
         *
         * @return Objeto {@link Connection} representando a conexão
         * com o banco de dados ou {@code null} em caso de falha.
         */

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