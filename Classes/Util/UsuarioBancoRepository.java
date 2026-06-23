package Classes.Util;

import Classes.Model.Operacoes.Status;
import Classes.Model.Usuario.Administrador;
import Classes.Model.Usuario.Cliente;
import Classes.Model.Usuario.Usuario;

import java.sql.*;
import java.util.ArrayList;

    /**
     * Classe responsável pela persistência dos usuários da aplicação.
     *
     * <p>Esta classe implementa operações de acesso ao banco de dados
     * relacionadas aos usuários do sistema, permitindo cadastrar,
     * atualizar, remover e carregar usuários para a memória da aplicação.</p>
     *
     * <p>Os registros recuperados são convertidos para objetos das classes
     * {@link Cliente} e {@link Administrador}, conforme o tipo de usuário
     * armazenado no banco de dados.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class UsuarioBancoRepository {

    /**
     * Carrega todos os usuários cadastrados no banco de dados e os adiciona
     * à coleção informada.
     *
     * <p>O método consulta a tabela de usuários e instancia objetos do tipo
     * {@link Cliente} ou {@link Administrador}, preenchendo seus atributos
     * com os dados recuperados.</p>
     *
     * @param listaMemoria lista que receberá os usuários carregados do banco.
     */

    public static void carregarUsuarios(ArrayList<Usuario> listaMemoria) {
        String sql = "SELECT * FROM usuario";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_usuario");
                Usuario u;

                if ("Cliente".equalsIgnoreCase(tipo)) {
                    u = new Cliente(
                            rs.getString("nome"), rs.getString("cpf"), rs.getString("email"),
                            rs.getString("senha"), rs.getString("data_nascimento"),
                            rs.getString("telefone"), tipo
                    );
                } else {
                    u = new Administrador(
                            rs.getString("nome"), rs.getString("cpf"), rs.getString("email"),
                            rs.getString("senha"), rs.getString("data_nascimento"),
                            rs.getString("telefone"), tipo, rs.getString("matricula")
                    );
                }

                u.setId(rs.getInt("id"));
                u.setStatus(Status.valueOf(rs.getString("status_perfil")));
                listaMemoria.add(u);
            }
            System.out.println("Banco de dados carregado com sucesso na memória!");
        } catch (SQLException e) {
            System.err.println("Erro ao carregar dados do banco: " + e.getMessage());
        }
    }

    /**
     * Remove um usuário do banco de dados utilizando o seu identificador.
     *
     * @param id identificador único do usuário a ser removido.
     */

    public static void deletarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Usuário removido do banco com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário do banco: " + e.getMessage());
        }
    }

    /**
     * Salva um novo usuário no banco de dados.
     *
     * <p>Após a inserção, o identificador gerado automaticamente pelo banco
     * é atribuído ao objeto em memória.</p>
     *
     * @param usuario usuário que será persistido.
     * @return {@code true} caso o cadastro seja realizado com sucesso;
     *         {@code false} caso ocorra algum erro durante a operação.
     */

    public static boolean salvarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha, data_nascimento, telefone, tipo_usuario, status_perfil, matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getDataNascimento());
            stmt.setString(6, usuario.getTelefone());
            stmt.setString(7, usuario.getTipoUsuario());
            stmt.setString(8, usuario.getStatus().name());

            if (usuario instanceof Administrador) {
                stmt.setString(9, ((Administrador) usuario).getMatricula());
            } else {
                stmt.setNull(9, Types.VARCHAR);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    usuario.setId(idGerado);
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar no banco: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza um atributo específico de um usuário no banco de dados.
     *
     * <p>O parâmetro {@code tipoDado} é utilizado para identificar qual
     * coluna deverá ser alterada na tabela de usuários.</p>
     *
     * <ul>
     *   <li>Nome</li>
     *   <li>CPF</li>
     *   <li>Email</li>
     *   <li>Senha</li>
     *   <li>Telefone</li>
     *   <li>DataNascimento</li>
     *   <li>Status</li>
     * </ul>
     *
     * @param id identificador do usuário que será atualizado.
     * @param tipoDado nome do atributo que será alterado.
     * @param novoValor novo valor a ser armazenado no banco.
     */

    public static void atualizarUsuario(int id, String tipoDado, String novoValor) {
        String colunaSql = "";
        if ("Nome".equalsIgnoreCase(tipoDado)) {
            colunaSql = "nome";
        } else if ("CPF".equalsIgnoreCase(tipoDado)) {
            colunaSql = "cpf";
        } else if ("Email".equalsIgnoreCase(tipoDado)) {
            colunaSql = "email";
        } else if ("Senha".equalsIgnoreCase(tipoDado)) {
            colunaSql = "senha";
        } else if ("Telefone".equalsIgnoreCase(tipoDado)) {
            colunaSql = "telefone";
        } else if ("DataNascimento".equalsIgnoreCase(tipoDado)) {
            colunaSql = "data_nascimento";
        } else if ("Status".equalsIgnoreCase(tipoDado)) {
            colunaSql = "status_perfil";
        } else {
            return;
        }

        String sql = "UPDATE usuario SET " + colunaSql + " = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoValor);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println("Coluna " + colunaSql + " updated no banco.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dado no banco: " + e.getMessage());
        }
    }
}
