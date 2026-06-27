import Classes.Model.Conta.Conta;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Justificativa;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;
import Classes.Model.Usuario.Administrador;
import Classes.Model.Usuario.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AdministradorTest {

    private Administrador admin;
    private Cliente cliente;
    private ContaCorrente conta;

    @BeforeEach
    void setUp() {
        admin = new Administrador("Admin Mestre", "000.000.000-00", "admin@fintech.com", "hash", "01/01/1980", "0000-0000", "Administrador", "MAT123");
        cliente = new Cliente("Carlos", "123.456.789-00", "carlos@email.com", "hash", "01/01/1990", "999", "Cliente");
        conta = new ContaCorrente("123", "001", 1000.0, "Conta Corrente", 500.0);
    }

    @Test
    void testAlterarDadosAdministrador() {
        admin.alterarDados("Matricula", "MAT999");
        assertEquals("MAT999", admin.getMatricula());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.alterarDados("Status", "INATIVO");
        });
        assertTrue(exception.getMessage().contains("Não é permitido alterar o status"));
    }

    @Test
    void testDesativarEAtivarConta() {
        // Desativação requer justificativa
        boolean desativou = admin.desativarConta(conta, Justificativa.SUSPEITA_FRAUDE);
        assertTrue(desativou);
        assertEquals(Status.INATIVO, conta.getStatus());

        // Tentativa de desativar sem justificativa
        assertThrows(IllegalArgumentException.class, () -> {
            admin.desativarConta(conta, null);
        });

        // Reativação
        boolean ativou = admin.ativarConta(conta);
        assertTrue(ativou);
        assertEquals(Status.ATIVO, conta.getStatus());
    }

    @Test
    void testDesativarEAtivarPerfilCliente() {
        boolean desativou = admin.desativarPerfilCliente(cliente, Justificativa.INATIVIDADE);
        assertTrue(desativou);
        assertEquals(Status.INATIVO, cliente.getStatus());

        boolean ativou = admin.ativarPerfilCliente(cliente);
        assertTrue(ativou);
        assertEquals(Status.ATIVO, cliente.getStatus());
    }

    @Test
    void testAnalisarPedidoLimite() {
        // Limite Atual: 1000, Saldo: 1000
        Cartao cartao = new Cartao("1111", "Carlos", "CREDITO", 1000.0, 1000.0);

        // Regra 1: Até 150% do limite atual (1000 * 1.5 = 1500)
        // Regra 2: No máximo 3x o saldo (1000 * 3.0 = 3000)
        // O teto final é o menor entre os dois: 1500.

        assertTrue(admin.analisarPedidoLimite(cartao, conta, 1500.0)); // No limite permitido
        assertFalse(admin.analisarPedidoLimite(cartao, conta, 1501.0)); // Acima de 150% do limite atual
    }

    @Test
    void testGerarRelatorioFintech() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente);
        
        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(conta);
        
        ArrayList<Transacao> transacoes = new ArrayList<>(); // Lista vazia para o teste
        
        String relatorio = admin.gerarRelatorioFintech(clientes, contas, transacoes);
        
        assertNotNull(relatorio);
        assertTrue(relatorio.contains("Total de Clientes Cadastrados: 1"));
        assertTrue(relatorio.contains("Montante Total em Custódia:    R$ 1000,00"));
    }
}