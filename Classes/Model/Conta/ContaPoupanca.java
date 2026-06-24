package Classes.Model.Conta;

import Classes.Exceptions.ContaInativaException;
import Classes.Exceptions.LimiteInsuficienteException;
import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

    /**
     * Representa uma conta poupança da fintech.
     *
     * <p>A conta poupança permite armazenar dinheiro com rendimento
     * automático sobre o saldo disponível. As movimentações podem ser
     * realizadas por PIX e Débito, não sendo permitido o uso da função
     * Crédito.</p>
     *
     * <p>Esta classe herda as funcionalidades básicas da classe
     * {@link Classes.Model.Conta.Conta} e adiciona o cálculo de rendimento periódico
     * característico das contas poupança.</p>
     *
     * @author Joel Antônio
     * @version 1.0
     * @since 2026
     */

public class ContaPoupanca extends Conta {
    private static final double taxaRendimento = 0.005;

    /**
     * Cria uma nova conta poupança.
     *
     * @param numeroConta Número identificador da conta.
     * @param agencia Agência à qual a conta pertence.
     * @param saldo Saldo inicial da conta.
     * @param tipoConta Tipo da conta.
     */

    public ContaPoupanca(String numeroConta, String agencia, double saldo, String tipoConta) {
        super(numeroConta, agencia, saldo, tipoConta);
    }

    /**
     * Método que implementa o RF23: Conta Poupança.
     *
     * <p>Aplica o rendimento da poupança sobre o saldo atual.</p>
     *
     * <p>O valor do rendimento é calculado utilizando a taxa
     * definida para a conta e adicionado ao saldo disponível.</p>
     *
     * <p>Após a aplicação do rendimento, uma transação de entrada
     * é registrada no extrato da conta para manter o histórico
     * da operação.</p>
     */

    public void aplicarRendimento() {
        LocalDate dataHoje = LocalDate.now();
        LocalTime horaAgora = LocalTime.now();
        YearMonth mesAtual = YearMonth.now();

        double valorRendimento = this.saldo * taxaRendimento;
        this.saldo += valorRendimento;

        String dataAtual = dataHoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaAtual = horaAgora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transacao transacaoRendimento = new Transacao(
                dataAtual,
                horaAtual,
                valorRendimento,
                "Rendimento Poupança",
                "ENTRADA",
                "RENDIMENTO",
                "CONCLUIDO",
                null,
                this
        );

        this.extrato.add(transacaoRendimento);
    }

    /**
     * Método que implementa o RF23: Conta Poupança.
     *
     * <p>Realiza uma transação financeira utilizando os recursos
     * disponíveis da conta poupança.</p>
     *
     * <p>São permitidos os seguintes métodos de pagamento:</p>
     *
     * <ul>
     *     <li>PIX;</li>
     *     <li>Cartão de Débito.</li>
     * </ul>
     *
     * <p>A função Crédito não está disponível para contas poupança.</p>
     *
     * <p>Após a conclusão da operação, uma transação de saída é
     * registrada na conta de origem e uma transação de entrada
     * é registrada na conta de destino.</p>
     *
     * @param valor Valor da transação.
     * @param metodoPagamento Método utilizado para pagamento.
     * @param cartaoEscolhido Cartão utilizado na operação.
     * @param categoria Categoria da movimentação financeira.
     * @param destino Conta que receberá o valor transferido.
     *
     * @return {@code true} caso a transação seja concluída com sucesso.
     *
     * @throws ContaInativaException Caso a conta de origem ou destino
     * esteja inativa.
     *
     * @throws SaldoInsuficienteException Caso o saldo da poupança
     * seja insuficiente para realizar a operação.
     *
     * @throws LimiteInsuficienteException Caso o cartão informado
     * esteja bloqueado.
     *
     * @throws IllegalArgumentException Caso seja informado um método
     * de pagamento inválido ou não suportado pela conta poupança.
     */

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: Sua conta poupança não está ativa.");}
        if (destino != null && destino.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");}
        if (destino == null) {throw new IllegalArgumentException("Conta de Destino não existe");}

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na Conta Poupança para realizar o PIX.");
            }
            this.saldo -= valor;
            destino.saldo += valor;


        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) {throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Débito.");}
            if (cartaoEscolhido.getEstaBloqueado()) {throw new LimiteInsuficienteException("Transação recusada: O cartão informado está bloqueado.");}
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Débito.");
            }

            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na Conta Poupança para realizar o Débito.");
            }
            this.saldo -= valor;
            destino.saldo += valor;


        } else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
            throw new IllegalArgumentException("Transação recusada: Conta Poupança não possui função Crédito disponível.");

        } else {
            throw new IllegalArgumentException("Método de pagamento inválido para poupança: " + metodoPagamento);
        }

        LocalDate dataHoje = LocalDate.now();
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAtual = dataHoje.format(formatadorData);

        LocalTime horaAgora = LocalTime.now();
        DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaAtual = horaAgora.format(formatadorHora);

        Transacao transacao = new Transacao(
                dataAtual,
                horaAtual,
                valor,
                categoria,
                "SAÍDA",
                metodoPagamento,
                "CONCLUIDO",
                this,
                destino
        );

        this.extrato.add(transacao);
        Transacao transacaoEntrada = new Transacao(dataAtual, horaAtual, valor, categoria, "ENTRADA", metodoPagamento, "CONCLUIDO", destino, this);
        destino.extrato.add(transacaoEntrada);

        return true;
    }

    /**
     * Método que implementa o RF06: Visualização de Dados da Conta.
     *
     * <p>Exibe os dados completos da conta poupança.</p>
     *
     * <p>Além das informações básicas fornecidas pela classe
     * {@link Classes.Model.Conta.Conta}, apresenta a taxa de rendimento
     * atualmente utilizada pela conta.</p>
     *
     * @return String formatada contendo os dados da conta
     * e a taxa de rendimento.
     */

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Taxa de Rendimento: %.2f%%\n", this.taxaRendimento * 100);
    }
}