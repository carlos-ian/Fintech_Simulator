package Classes.Exceptions;

    /**
     * Exceção lançada quando uma operação financeira é solicitada
     * sem que haja saldo suficiente para sua execução.
     *
     * <p>Utilizada em operações como transferências, pagamentos,
     * investimentos e demais movimentações que dependem de saldo
     * disponível na conta.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class SaldoInsuficienteException extends Exception {

    /**
     * Cria uma nova exceção de saldo insuficiente.
     *
     * @param mensagem mensagem detalhando o motivo da exceção.
     */

    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}