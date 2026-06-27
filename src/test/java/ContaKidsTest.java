import Classes.Exceptions.LimiteInsuficienteException;
import Classes.Model.Conta.ContaKids;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Operacoes.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaKidsTest {

    private ContaKids contaKids;
    private ContaCorrente contaDestino;

    @BeforeEach
    void setUp() {
        contaKids = new ContaKids("9999", "0001", 500.0, "KIDS", "123.456.789-00", 200.0);
        contaDestino = new ContaCorrente("54321", "0001", 0.0, "CORRENTE", 0.0);
    }

    @Test
    void testTransacaoAcimaDoLimiteMensal() {
        assertThrows(LimiteInsuficienteException.class, () -> {
            contaKids.realizarTransacao(250.0, "PIX", null, "Lanche", contaDestino);
        });
    }

    @Test
    void testTransacaoCreditoNaoPermitida() {
        Cartao cartao = new Cartao("2222", "Kid", "DEBITO", 0, 0);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaKids.realizarTransacao(50.0, "CREDITO", cartao, "Jogo", contaDestino);
        });
        
        assertTrue(exception.getMessage().contains("não possuem a função Crédito"));
    }

    @Test
    void testResetarMes() throws Exception {
        contaKids.realizarTransacao(150.0, "PIX", null, "Lanche", contaDestino);
        contaKids.resetarMes();
        
        // Deve permitir nova transação após o reset, pois o limite de 200 foi restaurado
        boolean sucesso = contaKids.realizarTransacao(100.0, "PIX", null, "Brinquedo", contaDestino);
        assertTrue(sucesso);
    }
}