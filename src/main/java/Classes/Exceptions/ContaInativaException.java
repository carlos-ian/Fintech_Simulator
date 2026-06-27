package Classes.Exceptions;

    /**
     * Exceção lançada quando uma operação é realizada em uma conta
     * que não está ativa para movimentações.
     *
     * <p>Utilizada para impedir transações em contas que estejam
     * bloqueadas, desativadas ou com status incompatível com a operação.</p>
     *
     * @author Brenno Soares
     * @version 1.0
     * @since 2026
     */

public class ContaInativaException extends Exception {

    /**
     * Cria uma nova exceção de conta inativa.
     *
     * @param mensagem mensagem detalhando o motivo da exceção.
     */

    public ContaInativaException(String mensagem) {
        super(mensagem);
    }
}