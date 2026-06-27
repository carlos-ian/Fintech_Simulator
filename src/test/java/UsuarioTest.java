import Classes.AplicacaoBancaria;
import Classes.Model.Usuario.Cliente;
import Classes.Model.Usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Cliente cliente;
    private String senhaPlana = "senha123";

    @BeforeEach
    void setUp() {
        // Inicializa a lista estática para os testes
        AplicacaoBancaria.listaUsuarios = new ArrayList<>();
        
        String senhaHash = BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
        cliente = new Cliente("João Silva", "111.222.333-44", "joao@email.com", senhaHash, "01/01/1990", "99999-9999", "Cliente");
        AplicacaoBancaria.listaUsuarios.add(cliente);
    }

    @Test
    void testAutenticarComSucesso() {
        Usuario usuarioLogado = Usuario.autenticar("111.222.333-44", senhaPlana);
        assertNotNull(usuarioLogado);
        assertEquals("João Silva", usuarioLogado.getNome());
    }

    @Test
    void testAutenticarComSenhaIncorreta() {
        Usuario usuarioLogado = Usuario.autenticar("111.222.333-44", "senhaErrada");
        assertNull(usuarioLogado);
    }

    @Test
    void testEncerrarPerfilComSenhaIncorreta() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cliente.encerrarPerfil(cliente, "senhaErrada");
        });
        assertTrue(exception.getMessage().contains("Senha incorreta"));
    }

    @Test
    void testEncerrarPerfilComContasComSaldo() {
        // Simula a abertura de uma conta com saldo
        cliente.abrirConta("123", "001", 100.0, "Conta Corrente", 0.0, null, 0.0, null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            cliente.encerrarPerfil(cliente, senhaPlana);
        });
        assertTrue(exception.getMessage().contains("possui saldo em conta"));
    }

    @Test
    void testAlterarDadosBase() {
        cliente.alterarDados("Nome", "João Santos");
        assertEquals("João Santos", cliente.getNome());
        
        cliente.alterarDados("Email", "joaosantos@email.com");
        assertEquals("joaosantos@email.com", cliente.getEmail());
    }
}