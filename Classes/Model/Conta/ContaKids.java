package Classes.Model.Conta;

import Classes.Model.Operacoes.Cartao;
import Classes.Exceptions.*;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaKids extends Conta {
    private String cpfResponsavel;
    private double limiteMensal;
    private double totalGastoNoMes;

    public ContaKids(String numeroConta, String agencia, double saldo, String tipoConta, String cpfResponsavel, double limiteMensal) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.cpfResponsavel = cpfResponsavel;
        this.limiteMensal = limiteMensal;
        this.totalGastoNoMes = 0.0;
    }

    public void resetarMes() {this.totalGastoNoMes = 0.0;}

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: Esta conta Kids não está ativa.");
        }
        if (destino != null && destino.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");
        }

        if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
            throw new IllegalArgumentException("Transação recusada: Contas para menores de idade não possuem a função Crédito disponível.");
        }

        if (this.totalGastoNoMes + valor > this.limiteMensal) {
            throw new LimiteInsuficienteException("Transação recusada: O limite mensal de gastos definido pelos seus responsáveis foi atingido.");
        }

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na sua Conta Kids.");
            }
            this.saldo -= valor;

        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) {
                throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Débito.");
            }
            if (cartaoEscolhido.getEstaBloqueado()) {
                throw new LimiteInsuficienteException("Transação recusada: Seu cartão está bloqueado.");
            }
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Débito.");
            }

            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na sua Conta Kids.");
            }
            this.saldo -= valor;

        } else {
            throw new IllegalArgumentException("Método de pagamento inválido para Conta Kids: " + metodoPagamento);
        }

        this.totalGastoNoMes += valor;

        if (destino != null) {destino.saldo += valor;}

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
        transacaoD.setTipoFluxo("ENTRADA");
        if (destino != null) {
            destino.extrato.add(transacaoD);
        }

        return true;
    }

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("CPF do Responsável: %s\nLimite Mensal: R$ %.2f\nTotal Gasto no Mês: R$ %.2f\n",
                        this.cpfResponsavel, this.limiteMensal, this.totalGastoNoMes);
    }
}