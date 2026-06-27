import Classes.Model.Conta.Conta;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Usuario.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Maria Oliveira", "555.666.777-88", "maria@email.com", "hash", "02/02/1985", "88888-8888", "Cliente");
    }

    @Test
    void testAbrirNovasContas() {
        Conta contaCorrente = cliente.abrirConta("1001", "001", 500.0, "Conta Corrente", 200.0, null, 0.0, null);
        assertNotNull(contaCorrente);
        assertTrue(contaCorrente instanceof ContaCorrente);
        assertEquals(1, cliente.obterContas().size());

        Conta contaPoupanca = cliente.abrirConta("1002", "001", 1000.0, "Conta Poupança", 0.0, null, 0.0, null);
        assertNotNull(contaPoupanca);
        assertEquals(2, cliente.obterContas().size());
    }

    @Test
    void testFecharContaSemSaldo() {
        Conta conta = cliente.abrirConta("1001", "001", 0.0, "Conta Corrente", 0.0, null, 0.0, null);
        
        boolean fechou = cliente.fecharConta(conta);
        assertTrue(fechou);
        assertEquals(0, cliente.obterContas().size());
    }

    @Test
    void testFecharContaComSaldoRejeitado() {
        Conta conta = cliente.abrirConta("1001", "001", 50.0, "Conta Corrente", 0.0, null, 0.0, null);
        
        boolean fechou = cliente.fecharConta(conta);
        assertFalse(fechou);
        assertEquals(1, cliente.obterContas().size());
    }

    @Test
    void testAlterarDadosProibidos() {
        IllegalArgumentException exceptionStatus = assertThrows(IllegalArgumentException.class, () -> {
            cliente.alterarDados("Status", "INATIVO");
        });
        assertTrue(exceptionStatus.getMessage().contains("Não é permitido alterar as contas ou o status"));

        IllegalArgumentException exceptionContas = assertThrows(IllegalArgumentException.class, () -> {
            cliente.alterarDados("Contas", "NovaConta");
        });
        assertTrue(exceptionContas.getMessage().contains("Não é permitido alterar as contas ou o status"));
    }
}