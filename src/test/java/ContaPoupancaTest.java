import Classes.Model.Conta.ContaPoupanca;
import Classes.Model.Conta.ContaCorrente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaPoupancaTest {

    private ContaPoupanca contaPoupanca;
    private ContaCorrente contaDestino;

    @BeforeEach
    void setUp() {
        contaPoupanca = new ContaPoupanca("7777", "0001", 1000.0, "POUPANCA");
        contaDestino = new ContaCorrente("54321", "0001", 0.0, "CORRENTE", 0.0);
    }

    @Test
    void testAplicarRendimento() {
        contaPoupanca.aplicarRendimento();
        // 1000 + (1000 * 0.005) = 1005
        assertEquals(1005.0, contaPoupanca.getSaldo(), 0.001);
        assertEquals(1, contaPoupanca.getExtrato().size());
    }

    @Test
    void testTransacaoCreditoRejeitada() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaPoupanca.realizarTransacao(100.0, "CREDITO", null, "Compra", contaDestino);
        });
        assertTrue(exception.getMessage().contains("não possui função Crédito"));
    }
}