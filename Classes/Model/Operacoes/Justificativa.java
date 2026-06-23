package Classes.Model.Operacoes;

    /**
     * Enumeração responsável por representar os motivos válidos para
     * bloqueio ou desativação de contas e perfis no sistema.
     *
     * <p>As justificativas são utilizadas principalmente por administradores
     * para registrar o motivo de uma ação administrativa, garantindo maior
     * controle, rastreabilidade e transparência das operações realizadas.</p>
     *
     * <p>Os valores disponíveis são:</p>
     * <ul>
     *     <li><b>SUSPEITA_FRAUDE</b> - Indica possível atividade fraudulenta.</li>
     *     <li><b>SOLICITACAO_CLIENTE</b> - Solicitação realizada pelo próprio cliente.</li>
     *     <li><b>INATIVIDADE</b> - Longo período sem utilização da conta ou perfil.</li>
     *     <li><b>ORDEM_JUDICIAL</b> - Determinação emitida por autoridade judicial competente.</li>
     * </ul>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public enum Justificativa {
    SUSPEITA_FRAUDE("Suspeita de atividade fraudulenta"),
    SOLICITACAO_CLIENTE("Solicitação do próprio cliente"),
    INATIVIDADE("Inatividade prolongada no sistema"),
    ORDEM_JUDICIAL("Determinação por ordem judicial");

    private final String descricao;

    Justificativa(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição legível da justificativa.
     *
     * <p>Este método sobrescreve o comportamento padrão do método
     * {@code toString()}, permitindo exibir a descrição amigável
     * da justificativa em vez do nome da constante do enum.</p>
     *
     * @return Descrição da justificativa.
     */

    @Override
    public String toString() {
        return this.descricao;
    }
}
