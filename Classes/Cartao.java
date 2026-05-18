public class Cartao {
    private String numeroCartao;
    private String titular;
    private String tipoCartao;
    private double limiteTotal;
    private double limiteDisponivel;
    private boolean estaBloqueado;

    Cartao(String numeroCartao, String titular, String tipoCartao, double limiteTotal, double limiteDisponivel, boolean estaBloqueado) {
        this.numeroCartao = numeroCartao;
        this.titular = titular;
        this.tipoCartao = tipoCartao;
        this.limiteTotal = limiteTotal;
        this.limiteDisponivel = limiteDisponivel;
        this.estaBloqueado = false;
    }

    // public String visualizarDados() {}
    // public boolean bloquearCartao() {}
    // public boolean ativarCartao() {}
    // public String visualizarLimite() {}
    // public void solicitarAjusteLimite() {]}
}
