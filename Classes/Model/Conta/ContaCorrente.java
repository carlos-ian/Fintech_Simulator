package Classes.Model.Conta;

import Classes.Model.Operacoes.Cartao;
import Classes.Exceptions.*;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

    /**
     * Representa uma conta-corrente da fintech.
     *
     * <p>A conta-corrente permite realizar transações financeiras utilizando
     * PIX, cartão de débito ou cartão de crédito. Além do saldo disponível,
     * o cliente também pode utilizar um limite de cheque especial quando
     * necessário.</p>
     *
     * <p>Esta classe herda as funcionalidades básicas da classe
     * {@link Classes.Model.Conta.Conta} e implementa regras específicas para movimentações
     * financeiras em contas correntes.</p>
     *
     * @author Joel Antônio
     * @version 1.0
     * @since 2026
     */

public class ContaCorrente extends Conta {
    private double limiteChequeEspecial;

    /**
     * Cria uma nova conta-corrente.
     *
     * @param numeroConta Número identificador da conta.
     * @param agencia Agência à qual a conta pertence.
     * @param saldo Saldo inicial da conta.
     * @param tipoConta Tipo da conta.
     * @param limiteChequeEspecial Limite disponível para cheque especial.
     */

    public ContaCorrente(String numeroConta, String agencia, double saldo, String tipoConta, double limiteChequeEspecial) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    /**
     * Método que implementa o RF15: Realização de Transações.
     *
     * <p>Realiza uma transação financeira.</p>
     *
     * <p>As transações podem ser efetuadas utilizando:</p>
     *
     * <ul>
     *     <li>PIX;</li>
     *     <li>Cartão de Débito;</li>
     *     <li>Cartão de Crédito.</li>
     * </ul>
     *
     * <p>Quando a operação é realizada por PIX ou Débito,
     * o valor é descontado diretamente do saldo da conta,
     * podendo utilizar o limite do cheque especial caso
     * necessário.</p>
     *
     * <p>Nas operações por Crédito, o valor é descontado
     * do limite disponível do cartão selecionado.</p>
     *
     * <p>Após a conclusão da operação, uma transação de saída
     * é registrada na conta de origem e uma transação de entrada
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
     * @throws Classes.Exceptions.ContaInativaException Caso a conta de origem ou destino
     * esteja inativa.
     *
     * @throws Classes.Exceptions.SaldoInsuficienteException Caso o saldo somado ao limite
     * do cheque especial seja insuficiente.
     *
     * @throws Classes.Exceptions.LimiteInsuficienteException Caso o limite do cartão seja
     * insuficiente para a operação.
     *
     * @throws IllegalArgumentException Caso algum parâmetro seja inválido.
     */

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: Sua conta não está ativa.");}
        if (destino != null && !destino.getStatus().equals(Status.ATIVO)) {throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");}
        if (destino == null) {throw new IllegalArgumentException("Conta de Destino não existe");}

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo + this.limiteChequeEspecial < valor) {
                throw new SaldoInsuficienteException("Saldo e limite de cheque especial insuficientes.");
            }
            this.saldo -= valor;
            destino.saldo += valor;


        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) throw new IllegalArgumentException("Informe o cartão.");
            if (cartaoEscolhido.getEstaBloqueado()) throw new IllegalArgumentException("Cartão bloqueado.");
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Este cartão não possui a função Débito.");
            }

            if (this.saldo + this.limiteChequeEspecial < valor) {
                throw new SaldoInsuficienteException("Saldo e limite de cheque especial insuficientes.");
            }
            this.saldo -= valor;
            destino.saldo += valor;

        } else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
            if (cartaoEscolhido == null) throw new IllegalArgumentException("Informe o cartão.");
            if (cartaoEscolhido.getEstaBloqueado()) throw new LimiteInsuficienteException("Cartão bloqueado.");
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("CREDITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Este cartão não possui a função Crédito.");
            }

            if (cartaoEscolhido.getLimiteDisponivel() < valor) {
                throw new LimiteInsuficienteException("Limite insuficiente no cartão.");
            }
            cartaoEscolhido.setLimiteDisponivel(cartaoEscolhido.getLimiteDisponivel() - valor);
            destino.saldo += valor;

        } else {
            throw new IllegalArgumentException("Método de pagamento inválido: " + metodoPagamento);
        }

        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transacao transacao = new Transacao(dataAtual, horaAtual, valor, categoria, "SAÍDA", metodoPagamento, "CONCLUIDO", this, destino);
        this.extrato.add(transacao);
        Transacao transacaoEntrada = new Transacao(dataAtual, horaAtual, valor, categoria, "ENTRADA", metodoPagamento, "CONCLUIDO", destino, this);
        destino.extrato.add(transacaoEntrada);

        return true;
    }

    /**
     * Método que implementa o RF06: Visualização de Dados da Conta.
     *
     * <p>Exibe os dados da conta corrente.</p>
     *
     * <p>Além das informações básicas fornecidas pela classe
     * {@link Classes.Model.Conta.Conta}, também apresenta o valor disponível
     * para utilização no cheque especial.</p>
     *
     * @return String formatada contendo os dados da conta.
     */

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Limite Cheque Especial: R$ %.2f\n", this.limiteChequeEspecial);
    }

    public double getLimiteChequeEspecial() {return this.limiteChequeEspecial;}
}