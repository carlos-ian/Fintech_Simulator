package Classes;

public enum Justificativa {
    SUSPEITA_FRAUDE("Suspeita de atividade fraudulenta"),
    SOLICITACAO_CLIENTE("Solicitação do próprio cliente"),
    INATIVIDADE("Inatividade prolongada no sistema"),
    ORDEM_JUDICIAL("Determinação por ordem judicial");

    private final String descricao;

    Justificativa(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
