package Classes.Model.Conta;

import Classes.Model.Operacoes.Cartao;
import Classes.Exceptions.*;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

    /**
     * Representa uma Conta Kids da fintech.
     *
     * <p>Esta modalidade de conta é destinada a menores de idade e possui
     * mecanismos de controle financeiro definidos pelos responsáveis legais,
     * como limite mensal de gastos e restrição ao uso da função crédito.</p>
     *
     * <p>As movimentações podem ser realizadas apenas por PIX e Débito,
     * respeitando o saldo disponível e o limite mensal configurado.</p>
     *
     * @author Joel Antônio
     * @version 1.0
     * @since 2026
     */

public class ContaKids extends Conta {
    private String cpfResponsavel;
    private double limiteMensal;
    private double totalGastoNoMes;

    /**
     * Cria uma nova Conta Kids.
     *
     * @param numeroConta Número identificador da conta.
     * @param agencia Agência à qual a conta pertence.
     * @param saldo Saldo inicial da conta.
     * @param tipoConta Tipo da conta.
     * @param cpfResponsavel CPF do responsável legal.
     * @param limiteMensal Limite máximo de gastos permitido por mês.
     */

    public ContaKids(String numeroConta, String agencia, double saldo, String tipoConta, String cpfResponsavel, double limiteMensal) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.cpfResponsavel = cpfResponsavel;
        this.limiteMensal = limiteMensal;
        this.totalGastoNoMes = 0.0;
    }

    /**
     * Método que implementa o RF24: Conta Kids.
     *
     * <p>Reinicia o controle de gastos mensais da conta.</p>
     *
     * <p>Este método deve ser utilizado no início de um novo mês para
     * permitir que o limite mensal seja reutilizado.</p>
     */

    public void resetarMes() {this.totalGastoNoMes = 0.0;}

    /**
     * Método que implementa o RF24: Conta Kids.
     *
     * <p>Realiza uma transação financeira utilizando os recursos da Conta Kids.</p>
     *
     * <p>São permitidos apenas os seguintes métodos de pagamento:</p>
     *
     * <ul>
     *     <li>PIX;</li>
     *     <li>Cartão de Débito.</li>
     * </ul>
     *
     * <p>A função Crédito não está disponível para este tipo de conta.</p>
     *
     * <p>Antes da operação, são realizadas validações de:</p>
     * <ul>
     *     <li>Status da conta de origem;</li>
     *     <li>Status da conta de destino;</li>
     *     <li>Saldo disponível;</li>
     *     <li>Limite mensal de gastos;</li>
     *     <li>Validade do cartão informado.</li>
     * </ul>
     *
     * <p>Após a conclusão da operação, uma transação de saída é registrada
     * na conta de origem e uma transação de entrada é registrada na conta
     * de destino.</p>
     *
     * @param valor Valor da transação.
     * @param metodoPagamento Método utilizado para pagamento.
     * @param cartaoEscolhido Cartão utilizado na operação.
     * @param categoria Categoria da movimentação financeira.
     * @param destino Conta que receberá o valor transferido.
     *
     * @return {@code true} caso a transação seja realizada com sucesso.
     *
     * @throws Classes.Exceptions.ContaInativaException Caso a conta de origem ou destino
     * esteja inativa.
     *
     * @throws Classes.Exceptions.SaldoInsuficienteException Caso não exista saldo suficiente
     * para realizar a operação.
     *
     * @throws Classes.Exceptions.LimiteInsuficienteException Caso o limite mensal de gastos
     * seja ultrapassado ou o cartão esteja bloqueado.
     *
     * @throws IllegalArgumentException Caso seja informado um método de
     * pagamento inválido ou não permitido para a Conta Kids.
     */

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: Esta conta Kids não está ativa.");}
        if (destino != null && destino.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");}
        if (metodoPagamento.equalsIgnoreCase("CREDITO")) {throw new IllegalArgumentException("Transação recusada: Contas para menores de idade não possuem a função Crédito disponível.");}
        if (destino == null) {throw new IllegalArgumentException("Conta de Destino não existe");}

        if (this.totalGastoNoMes + valor > this.limiteMensal) {throw new LimiteInsuficienteException("Transação recusada: O limite mensal de gastos definido pelos seus responsáveis foi atingido.");}

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo < valor) {throw new SaldoInsuficienteException("Saldo insuficiente na sua Conta Kids.");}
            this.saldo -= valor;
            destino.saldo += valor;

        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) {throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Débito.");}
            if (cartaoEscolhido.getEstaBloqueado()) {throw new LimiteInsuficienteException("Transação recusada: Seu cartão está bloqueado.");}
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Débito.");}
            if (this.saldo < valor) {throw new SaldoInsuficienteException("Saldo insuficiente na sua Conta Kids.");}

            this.saldo -= valor;
            destino.saldo += valor;

        } else {throw new IllegalArgumentException("Método de pagamento inválido para Conta Kids: " + metodoPagamento);}

        this.totalGastoNoMes += valor;

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

        Transacao transacaoD = transacao;
        this.extrato.add(transacao);
        Transacao transacaoEntrada = new Transacao(dataAtual, horaAtual, valor, categoria, "ENTRADA", metodoPagamento, "CONCLUIDO", destino, this);
        destino.extrato.add(transacaoEntrada);

        return true;
    }

    /**
     * Método que implementa o RF06: Visualização de Dados da Conta.
     *
     * <p>Exibe os dados completos da Conta Kids.</p>
     *
     * <p>Além das informações básicas fornecidas pela classe
     * {@link Classes.Model.Conta.Conta}, são exibidos os dados do responsável legal,
     * o limite mensal definido e o total gasto no mês atual.</p>
     *
     * @return String formatada contendo os dados da conta.
     */

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("CPF do Responsável: %s\nLimite Mensal: R$ %.2f\nTotal Gasto no Mês: R$ %.2f\n",
                        this.cpfResponsavel, this.limiteMensal, this.totalGastoNoMes);
    }

    public String getCpfResponsavel() {return this.cpfResponsavel;}
    public double getLimiteMensal() {return this.limiteMensal;}
}