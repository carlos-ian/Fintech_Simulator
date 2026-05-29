package Classes;

public class Cartao {
    private String numeroCartao;
    private String titular;
    private String tipoCartao;
    private double limiteTotal;
    private double limiteDisponivel;
    private boolean estaBloqueado;

    Cartao(String numeroCartao, String titular, String tipoCartao, double limiteTotal, double limiteDisponivel) {
        this.numeroCartao = numeroCartao;
        this.titular = titular;
        this.tipoCartao = tipoCartao;
        this.limiteTotal = limiteTotal;
        this.limiteDisponivel = limiteDisponivel;
        this.estaBloqueado = false;
    }

    public String visualizarDados() {
        String numeroMascarado = "**** **** **** ";
        if (this.numeroCartao != null && this.numeroCartao.length() >= 4) {
            numeroMascarado += this.numeroCartao.substring(this.numeroCartao.length() - 4);
        } else {
            numeroMascarado = this.numeroCartao;
        }

        String status = this.estaBloqueado ? "BLOQUEADO" : "ATIVO";

        String dados = "\n============== INFORMAÇÕES DO CARTÃO ==============\n";
        dados += "Titular:           " + this.titular + "\n";
        dados += "Número do Cartão:  " + numeroMascarado + "\n";
        dados += "Tipo de Cartão:    " + this.tipoCartao.toUpperCase() + "\n";
        dados += "Status Atual:      " + status + "\n";

        if (this.tipoCartao.equalsIgnoreCase("CREDITO") || this.tipoCartao.equalsIgnoreCase("AMBOS")) {
            dados += String.format("Limite Total:      R$ %.2f\n", this.limiteTotal);
            dados += String.format("Limite Disponível: R$ %.2f\n", this.limiteDisponivel);
        }
        dados += "===================================================";

        return dados;
    }

    public boolean bloquearCartao() {
        if (this.estaBloqueado) {return false;}
        this.estaBloqueado = true;
        return true;
    }

    public boolean ativarCartao() {
        if (!this.estaBloqueado) {return false;}
        this.estaBloqueado = false;
        return true;
    }

    public String visualizarLimites() {
        if (this.tipoCartao.equalsIgnoreCase("DEBITO")) {
            return "\n============== LIMITE DO CARTÃO ==============\n" +
                    "Este cartão possui apenas a função DÉBITO.\n" +
                    "O saldo utilizável depende diretamente do saldo da conta.\n" +
                    "==============================================";
        }

        double limiteUtilizado = this.limiteTotal - this.limiteDisponivel;
        String textoLimite = String.format(
                "\n============== LIMITE DO CARTÃO ==============\n" +
                        "Titular:           %s\n" +
                        "Limite Total:      R$ %.2f\n" +
                        "Limite Disponível: R$ %.2f\n" +
                        "Limite Utilizado:  R$ %.2f\n" +
                        "==============================================",
                this.titular, this.limiteTotal, this.limiteDisponivel, limiteUtilizado
        );
        return textoLimite;
    }

    public boolean solicitarAjusteLimite(double novoLimiteSolicitado, Administrador admin, Conta contaVinculada) {
        if (this.tipoCartao.equalsIgnoreCase("DEBITO")) {
            throw new IllegalArgumentException("Não é possivel solicitar ajuste de limite com cartão Débito");
        }

        if (novoLimiteSolicitado == 0) {return false;}

        boolean resultadoAnalise = admin.analisarPedidoLimite(this, contaVinculada, novoLimiteSolicitado);
        if (resultadoAnalise) {
            double limiteUtilizado = this.limiteTotal - this.limiteDisponivel;
            this.limiteTotal = novoLimiteSolicitado;
            this.limiteDisponivel = this.limiteTotal - limiteUtilizado;
            return true;
        }

        return false;
    }

    public double getLimiteTotal() { return this.limiteTotal; }
    public String getTipoCartao() {return this.tipoCartao;}
    public double getLimiteDisponivel() {return limiteDisponivel;}
    public boolean getEstaBloqueado() {return this.estaBloqueado;}
    public void setLimiteDisponivel (double limiteDisponivel) {this.limiteDisponivel = limiteDisponivel;}
}
