package Classes.Model.Operacoes;

    /**
     * Enumeração responsável por representar os possíveis estados
     * de entidades do sistema, como usuários, contas, cartões
     * e demais recursos que podem ser ativados ou desativados.
     *
     * <p>Os valores disponíveis são:</p>
     * <ul>
     *     <li><b>ATIVO</b>: indica que a entidade está habilitada
     *     para utilização e operações.</li>
     *     <li><b>INATIVO</b>: indica que a entidade está desabilitada,
     *     impedindo a realização de operações.</li>
     * </ul>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public enum Status {
    ATIVO,
    INATIVO,
}
