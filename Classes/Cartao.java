package Classes;

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

    public void visualizarDados() {
        // 1. Exibição Segura do Número do Cartão (Apenas Últimos 4 dígitos)
        String numeroMascarado = "**** **** **** ";
        if (this.numeroCartao != null && this.numeroCartao.length() >= 4) {
            numeroMascarado += this.numeroCartao.substring(this.numeroCartao.length() - 4);
        } else {
            numeroMascarado = this.numeroCartao;
        }

        // 2. Traduz o booleano de bloqueio para Texto
        String status = this.estaBloqueado ? "BLOQUEADO" : "ATIVO";

        // 3. Visualização das Informações
        System.out.println("\n============== INFORMAÇÕES DO CARTÃO ==============");
        System.out.printf("Titular:          %s\n", this.titular);
        System.out.printf("Número do Cartão: %s\n", numeroMascarado);
        System.out.printf("Tipo de Cartão:   %s\n", this.tipoCartao.toUpperCase());
        System.out.printf("Status Atual:     %s\n", status);

        // 4. Impressão Adicional de Limites para Cartão de Crédito ou Ambos
        if (this.tipoCartao.equalsIgnoreCase("CREDITO") || this.tipoCartao.equalsIgnoreCase("AMBOS")) {
            System.out.printf("Limite Total:     R$ %.2f\n", this.limiteTotal);
            System.out.printf("Limite Disponível: R$ %.2f\n", this.limiteDisponivel);
        }
        System.out.println("===================================================");
    }

    public boolean bloquearCartao() {
        if (this.estaBloqueado) {
            System.out.println("Aviso: Este cartão já está bloqueado!");
            return false;
        }
        this.estaBloqueado = true;
        System.out.println("Sucesso: O cartão foi bloqueado com sucesso.");
        return true;
    }

    public boolean ativarCartao() {
        if (!this.estaBloqueado) {
            System.out.println("Aviso: Este cartão já está ativo e pronto para uso!");
            return false;
        }
        this.estaBloqueado = false;
        System.out.println("Sucesso: O cartão foi ativado e desbloqueado.");
        return true;
    }

    public void visualizarLimite() {
        if (this.tipoCartao.equalsIgnoreCase("DEBITO")) {
            System.out.println("\n============== LIMITE DO CARTÃO ==============");
            System.out.println("Este cartão possui apenas a função DÉBITO.");
            System.out.println("O saldo utilizável depende diretamente do saldo da conta.");
            System.out.println("==============================================");
            return;
        }

        System.out.println("\n============== LIMITE DO CARTÃO ==============");
        System.out.printf("Titular:           %s\n", this.titular);
        System.out.printf("Limite Total:      R$ %.2f\n", this.limiteTotal);
        System.out.printf("Limite Disponível: R$ %.2f\n", this.limiteDisponivel);
        System.out.printf("Limite Utilizado:  R$ %.2f\n", (this.limiteTotal - this.limiteDisponivel));
        System.out.println("==============================================");
    }

    public void solicitarAjusteLimite(double novoLimiteSolicitado) {
        // 1. Validação para Cartão de Débito
        if (this.tipoCartao.equalsIgnoreCase("DEBITO")) {
            System.out.println("Erro: Não é possível solicitar limite de crédito para um cartão puramente de DÉBITO.");
            return;
        }

        // 2. Validação para que o valor solicitado seja positivo
        if (novoLimiteSolicitado <= 0) {
            System.out.println("Erro: O valor do limite solicitado deve ser maior que R$ 0,00.");
            return;
        }

        // 3. Mostra se a Solicitação é de Aumento ou de Diminuição
        if (novoLimiteSolicitado > this.limiteTotal) {
            System.out.println("Tipo de Pedido: Aumento de Limite");
        } else if (novoLimiteSolicitado < this.limiteTotal) {
            System.out.println("Tipo de Pedido: Redução de Limite");
        } else {
            System.out.println("Tipo de Pedido: Manter Limite Igual");
        }

        // 4. Envio de Solicitação para Administrador
        boolean resultadoAnalise = admin.analisarPedidoLimite(this, novoLimiteSolicitado);
        if (resultadoAnalise) {
            System.out.println("[SISTEMA] Notificação para o cliente: Seu novo limite já está disponível!");
            this.limiteTotal = novoLimiteSolicitado;
            this.setLimiteDisponivel(novoLimiteSolicitado - this.limiteDisponivel);
        } else {
             System.out.println("[SISTEMA] Notificação para o cliente: Sua solicitação foi recusada pelo Administrador.");
        }

    }

    public String getTipoCartao() {return this.tipoCartao;}
    public double getLimiteDisponivel() {return limiteDisponivel;}
    public boolean getEstaBloqueado() {return this.estaBloqueado;}
    public void setLimiteDisponivel (double limiteDisponivel) {this.limiteDisponivel = limiteDisponivel;}
}
