package Classes.Util;

import Classes.Model.Operacoes.Status;
import Classes.Model.Usuario.Administrador;
import Classes.Model.Usuario.Cliente;
import Classes.Model.Usuario.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioBancoRepository {
    public static void carregarUsuariosParaMemoria(ArrayList<Usuario> listaMemoria) {
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

                u.setStatus(Status.valueOf(rs.getString("status_perfil")));
                listaMemoria.add(u);
            }
            System.out.println("Banco de dados carregado com sucesso na memória!");
        } catch (SQLException e) {
            System.err.println("Erro ao carregar dados do banco: " + e.getMessage());
        }
    }

    public static void salvarNoBanco(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha, data_nascimento, telefone, tipo_usuario, status_perfil, matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        } catch (SQLException e) {
            System.err.println("Erro ao salvar no banco: " + e.getMessage());
        }
    }
}
