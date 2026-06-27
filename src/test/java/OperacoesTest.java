import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Investimento;
import Classes.Model.Conta.ContaCorrente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OperacoesTest {

    @Test
    void testBloqueioEDesbloqueioDeCartao() {
        Cartao cartao = new Cartao("1234567890123456", "Ian", "CREDITO", 2000.0, 2000.0);
        
        assertFalse(cartao.getEstaBloqueado());
        
        assertTrue(cartao.bloquearCartao());
        assertTrue(cartao.getEstaBloqueado());
        assertFalse(cartao.bloquearCartao()); // Já está bloqueado
        
        assertTrue(cartao.ativarCartao());
        assertFalse(cartao.getEstaBloqueado());
    }

    @Test
    void testAplicarRendimentoInvestimento() {
        Investimento inv = new Investimento("CDB", 0.10, 1000.0, "01/01/2026");
        inv.aplicarRendimento();
        
        // 1000 + 10% = 1100
        assertEquals(1100.0, inv.getValorAplicado(), 0.001);
    }

    @Test
    void testRealizarEResgatarInvestimento() throws Exception {
        ContaCorrente conta = new ContaCorrente("111", "001", 5000.0, "CORRENTE", 0.0);
        Investimento inv = new Investimento("Tesouro Direto", 0.05, 0, ""); // Valor 0 inicial
        
        // Realizar
        boolean investido = inv.realizarInvestimento(conta, inv, 2000.0, "25/06/2026");
        assertTrue(investido);
        assertEquals(3000.0, conta.getSaldo());
        assertEquals(2000.0, inv.getValorAplicado());
        assertEquals(1, conta.getListaInvestimentos().size());
        
        // Resgatar
        boolean resgatado = inv.resgatarInvestimento(conta, inv.getId());
        assertTrue(resgatado);
        assertEquals(5000.0, conta.getSaldo());
        assertEquals(0, conta.getListaInvestimentos().size());
    }
}