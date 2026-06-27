import Classes.Model.Conta.ContaInvestimento;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Operacoes.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaInvestimentoTest {

    private ContaInvestimento contaInvestimento;
    private ContaCorrente contaDestino;

    @BeforeEach
    void setUp() {
        contaInvestimento = new ContaInvestimento("8888", "0001", 5000.0, "INVESTIMENTO", "111.222.333-44");
        contaDestino = new ContaCorrente("54321", "0001", 0.0, "CORRENTE", 0.0);
    }

    @Test
    void testResgateInvestimentoComSucesso() throws Exception {
        boolean sucesso = contaInvestimento.realizarTransacao(1000.0, "TRANSFERENCIA", null, "Resgate", contaDestino);
        
        assertTrue(sucesso);
        assertEquals(4000.0, contaInvestimento.getSaldo());
        assertEquals(1000.0, contaDestino.getSaldo());
    }

    @Test
    void testTransacaoComCartaoRejeitada() {
        Cartao cartao = new Cartao("3333", "Titular", "DEBITO", 0, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            contaInvestimento.realizarTransacao(500.0, "DEBITO", cartao, "Compra", contaDestino);
        });
    }
}