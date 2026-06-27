package Classes.Exceptions;

    /**
     * Exceção lançada quando o limite disponível para uma operação
     * não é suficiente para sua realização.
     *
     * <p>Pode ocorrer em operações envolvendo cartão de crédito,
     * cheque especial ou limites previamente definidos pelo sistema.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class LimiteInsuficienteException extends Exception {

    /**
     * Cria uma nova exceção de limite insuficiente.
     *
     * @param mensagem mensagem detalhando o motivo da exceção.
     */

    public LimiteInsuficienteException(String mensagem) {
        super(mensagem);
    }
}