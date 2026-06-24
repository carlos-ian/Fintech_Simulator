package Classes.Model.Conta;

import Classes.Exceptions.ContaInativaException;
import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

    /**
     * Representa uma conta de investimento da fintech.
     *
     * <p>Este tipo de conta é destinado ao armazenamento de recursos aplicados
     * em investimentos. As movimentações permitidas consistem apenas em
     * transferências de valores para outras contas do sistema, simulando
     * operações de resgate de investimentos.</p>
     *
     * <p>Diferentemente de outras modalidades de conta, a Conta Investimento
     * não permite operações utilizando cartões de crédito ou débito.</p>
     *
     * @author Joel Antônio
     * @version 1.0
     * @since 2026
     */

public class ContaInvestimento extends Conta {
    private String titularCPF;

    /**
     * Cria uma nova conta de investimento.
     *
     * @param numeroConta Número identificador da conta.
     * @param agencia Agência à qual a conta pertence.
     * @param saldo Saldo inicial disponível para investimentos.
     * @param tipoConta Tipo da conta.
     * @param titular CPF do titular da conta.
     */

    public ContaInvestimento(String numeroConta, String agencia, double saldo, String tipoConta, String titular) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.titularCPF = titular;
    }

    /**
     * Método que implementa o RF24: Conta Investimento.
     *
     * <p>Realiza uma movimentação financeira na conta de investimento.</p>
     *
     * <p>Esta operação representa o resgate de recursos investidos,
     * transferindo um valor da conta de investimento para uma conta
     * de destino informada.</p>
     *
     * <p>Por regra de negócio, contas de investimento não permitem
     * operações utilizando cartões. Caso um cartão seja informado,
     * a operação será recusada.</p>
     *
     * <p>Após a conclusão da operação, uma transação de saída é
     * registrada no extrato da conta de investimento e uma
     * transação de entrada é registrada na conta de destino.</p>
     *
     * @param valor Valor a ser transferido.
     * @param metodoPagamento Método utilizado na movimentação.
     * @param cartaoEscolhido Cartão informado para a operação.
     *                        Deve ser {@code null}.
     * @param categoria Categoria da transação.
     * @param destino Conta que receberá o valor transferido.
     *
     * @return {@code true} caso a movimentação seja concluída
     * com sucesso.
     *
     * @throws ContaInativaException Caso a conta de investimento
     * esteja inativa ou não exista uma conta de destino válida.
     *
     * @throws SaldoInsuficienteException Caso o saldo disponível
     * seja insuficiente para realizar a operação.
     *
     * @throws IllegalArgumentException Caso seja informado um
     * cartão para a movimentação.
     */

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException {

        if (this.getStatus() != Status.ATIVO) {throw new ContaInativaException("Conta Investimento inativa.");}
        if (cartaoEscolhido != null) {throw new IllegalArgumentException("Conta Investimento não permite operações com cartão.");}
        if (destino == null) {throw new ContaInativaException("Para movimentar o investimento, informe a conta corrente de destino.");}

        if (valor > this.getSaldo()) {throw new SaldoInsuficienteException("Saldo insuficiente no fundo de investimento.");}

        this.saldo -= valor;
        destino.saldo += valor;

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
     * <p>Exibe os dados completos da conta de investimento.</p>
     *
     * <p>Além das informações básicas fornecidas pela classe
     * {@link Classes.Model.Conta.Conta}, apresenta o CPF do titular responsável
     * pela conta.</p>
     *
     * @return String formatada contendo os dados da conta.
     */

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Perfil de Risco: %s\n", this.titularCPF);
    }

    public String getTitularCPF() {return this.titularCPF;}
}
