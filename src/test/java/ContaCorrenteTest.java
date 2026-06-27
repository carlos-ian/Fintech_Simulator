import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Operacoes.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaCorrenteTest {

    private ContaCorrente contaOrigem;
    private ContaCorrente contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new ContaCorrente("12345", "0001", 1000.0, "CORRENTE", 500.0);
        contaDestino = new ContaCorrente("54321", "0001", 0.0, "CORRENTE", 0.0);
    }

    @Test
    void testRealizarTransacaoPixComSucesso() throws Exception {
        boolean sucesso = contaOrigem.realizarTransacao(200.0, "PIX", null, "Transferência", contaDestino);
        
        assertTrue(sucesso);
        assertEquals(800.0, contaOrigem.getSaldo());
        assertEquals(200.0, contaDestino.getSaldo());
    }

    @Test
    void testRealizarTransacaoPixUsandoChequeEspecial() throws Exception {
        // Saldo de 1000 + 500 de limite
        boolean sucesso = contaOrigem.realizarTransacao(1200.0, "PIX", null, "Pagamento", contaDestino);
        
        assertTrue(sucesso);
        assertEquals(-200.0, contaOrigem.getSaldo());
        assertEquals(1200.0, contaDestino.getSaldo());
    }

    @Test
    void testRealizarTransacaoPixSemSaldoELimite() {
        assertThrows(SaldoInsuficienteException.class, () -> {
            contaOrigem.realizarTransacao(1600.0, "PIX", null, "Compra", contaDestino);
        });
    }

    @Test
    void testTransacaoCreditoComCartao() throws Exception {
        Cartao cartao = new Cartao("1111", "Ian", "CREDITO", 1000.0, 1000.0);
        
        boolean sucesso = contaOrigem.realizarTransacao(300.0, "CREDITO", cartao, "Compra", contaDestino);
        
        assertTrue(sucesso);
        assertEquals(1000.0, contaOrigem.getSaldo()); // Saldo não muda no crédito
        assertEquals(700.0, cartao.getLimiteDisponivel());
        assertEquals(300.0, contaDestino.getSaldo());
    }
}