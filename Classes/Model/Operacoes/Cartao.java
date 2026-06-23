package Classes.Model.Operacoes;

import Classes.Model.Conta.Conta;
import Classes.Model.Usuario.Administrador;

    /**
     * Representa um cartão bancário vinculado a uma conta do sistema.
     *
     * <p>O cartão pode possuir função de débito, crédito ou ambas,
     * permitindo a realização de transações financeiras conforme
     * as suas características e limites disponíveis.</p>
     *
     * <p>A classe também oferece funcionalidades para consulta de
     * informações, gestão de bloqueio e solicitação de ajuste
     * de limite de crédito.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class Cartao {
    private String numeroCartao;
    private String titular;
    private String tipoCartao;
    private double limiteTotal;
    private double limiteDisponivel;
    private boolean estaBloqueado;

    /**
     * Cria um novo cartão bancário.
     *
     * @param numeroCartao Número do cartão.
     * @param titular Nome do titular.
     * @param tipoCartao Tipo do cartão (Débito, Crédito ou Ambos).
     * @param limiteTotal Limite total do cartão.
     * @param limiteDisponivel Limite disponível para utilização.
     */

    public Cartao(String numeroCartao, String titular, String tipoCartao, double limiteTotal, double limiteDisponivel) {
        this.numeroCartao = numeroCartao;
        this.titular = titular;
        this.tipoCartao = tipoCartao;
        this.limiteTotal = limiteTotal;
        this.limiteDisponivel = limiteDisponivel;
        this.estaBloqueado = false;
    }

    /**
     * Método que implementa o RF12: Visualizar Dados do Cartão.
     *
     * <p>Retorna uma representação textual contendo as principais
     * informações do cartão.</p>
     *
     * <p>Por segurança, apenas os quatro últimos dígitos do número
     * do cartão são exibidos.</p>
     *
     * @return String formatada contendo os dados do cartão.
     */

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

    /**
     * Método que implementa o RF13: Bloquear e Desbloquear Cartão.
     *
     * <p>Bloqueia o cartão para impedir novas operações.</p>
     *
     * @return {@code true} caso o cartão seja bloqueado com sucesso;
     * {@code false} caso ele já esteja bloqueado.
     */

    public boolean bloquearCartao() {
        if (this.estaBloqueado) {return false;}
        this.estaBloqueado = true;
        return true;
    }

    /**
     * Método que implementa o RF13: Bloquear e Desbloquear Cartão.
     *
     * <p>Reativa um cartão previamente bloqueado.</p>
     *
     * @return {@code true} caso o cartão seja ativado com sucesso;
     * {@code false} caso ele já esteja ativo.
     */

    public boolean ativarCartao() {
        if (!this.estaBloqueado) {return false;}
        this.estaBloqueado = false;
        return true;
    }

    /**
     * Método que implementa o RF14: Visualização e Ajuste de Limite.
     *
     * <p>Exibe informações relacionadas aos limites do cartão.</p>
     *
     * <p>Para cartões exclusivamente de débito, informa que o valor
     * disponível depende diretamente do saldo da conta vinculada.</p>
     *
     * <p>Para cartões com função crédito, apresenta limite total,
     * disponível e utilizado.</p>
     *
     * @return String contendo os dados de limite do cartão.
     */

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

    /**
     * Método que implementa o RF14: Visualização e Ajuste de Limite.
     *
     * <p>Solicita um ajuste no limite do cartão.</p>
     *
     * <p>A solicitação é analisada por um administrador com base
     * nas regras de negócio da fintech e nas informações da conta
     * vinculada ao cartão.</p>
     *
     * <p>Caso aprovada, os limites do cartão são atualizados
     * automaticamente.</p>
     *
     * @param novoLimiteSolicitado Novo limite desejado.
     * @param admin Administrador responsável pela análise.
     * @param contaVinculada Conta associada ao cartão.
     *
     * @return {@code true} caso a solicitação seja aprovada;
     * {@code false} caso seja rejeitada.
     *
     * @throws IllegalArgumentException Caso o cartão possua apenas
     * função débito.
     */

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
    public String getNumeroCartao() {return numeroCartao;}
    public String getTitular() {return titular;}
}
