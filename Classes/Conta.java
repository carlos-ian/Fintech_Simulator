import java.util.ArrayList;

public abstract class Conta {
    protected ArrayList<Transacao> extrato;
    protected ArrayList<Cartao> cartoes;
    // protected ArrayList<Investimentos> listaInvestimentos;
    // protected Poupanca poupanca;

    protected String numeroConta;
    protected String agencia;
    protected double saldo;
    protected String tipoConta;
    protected String statusConta;

    Conta(String numeroConta, String agencia, double saldo, String tipoConta, String statusConta) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = 0;
        this.tipoConta = tipoConta;
        this.statusConta = "Ativo";
    }

    public boolean realizarTransacao(double valor, String metodoPagamento, String categoria, Conta destino) throws ContaInativaException, SaldoInsuficienteException {

            // 1. Verificação de Cenários: Conta de Origem está Ativa, Conta de Destino Existe e está Ativa
            if (!this.statusConta.equalsIgnoreCase("Ativo")) {
                throw new ContaInativaException("Transação recusada: Sua conta não está ativa.");
            }
            if (destino != null && !destino.statusConta.equalsIgnoreCase("Ativo")) {
                throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");
            }

            // 2. Realização da Transferência
            if (metodoPagamento.equalsIgnoreCase("PIX") || metodoPagamento.equalsIgnoreCase("DEBITO")) {
                if (this.saldo < valor) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar o " + metodoPagamento + ".");
                }
                this.saldo -= valor;
            }
            else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
                System.out.println("Processando pagamento via Crédito (validando limite do cartão)...");
                // Implementação da Lógica de Limite depois
            } else {
                throw new IllegalArgumentException("Método de pagamento inválido: " + metodoPagamento);
            }

            // 3. Recebimento da Transferência
            if (destino != null && (metodoPagamento.equalsIgnoreCase("PIX") || metodoPagamento.equalsIgnoreCase("DEBITO"))) {
                destino.saldo += valor;
            }

            // 4. Instaciação da Transferência
            Transacao transacao = new Transacao(
                    dataAtual, // Adicionar Lógica de Data Atual depois
                    horaAtual, // Adicionar Lógica de Hora Atual depois
                    valor,
                    categoria,
                    "SAÍDA",
                    metodoPagamento,
                    "CONCLUIDO",
                    this,
                    destino
            );

            // 5. Adiciona Transação ao Extrato de Ambos
            this.extrato.add(transacao);
            if (destino != null) {
                transacao.setTipoFluxo("ENTRADA");
                destino.extrato.add(transacao);
            }

            System.out.println("Transação registrada com sucesso ID: " + transacao.getId());
            return true;
        }

        // public boolean receberTransacao(double valor, Conta origem, Conta destino) {}
        // public ArrayList<Transacao> visualizarExtrato(int periodo) {}
}

