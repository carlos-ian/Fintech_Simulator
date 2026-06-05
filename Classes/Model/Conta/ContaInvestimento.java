package Classes.Model.Conta;

import Classes.Exceptions.ContaInativaException;
import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaInvestimento extends Conta {
    private String titularCPF;

    public ContaInvestimento(String numeroConta, String agencia, double saldo, String tipoConta, String titular) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.titularCPF = titular;
    }

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

        Transacao transacaoD = transacao;
        this.extrato.add(transacao);
        transacaoD.setTipoFluxo("ENTRADA");
        destino.extrato.add(transacaoD);

        return true;
    }

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Perfil de Risco: %s\n", this.titularCPF);
    }

    public String getTitularCPF() {return this.titularCPF;}
}
