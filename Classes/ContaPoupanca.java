package Classes;

import Classes.Exceptions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaPoupanca extends Conta {
    private double taxaRendimento;

    ContaPoupanca(String numeroConta, String agencia, double saldo, String tipoConta, double taxaRendimento) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.taxaRendimento = taxaRendimento;
    }

    // Metodo que Talvez será Retirado
    public void aplicarRendimento() {
        if (this.statusConta != Status.ATIVO) {
            System.out.println("Não foi possível aplicar rendimento: Conta inativa.");
            return;
        }

        double valorRendimento = this.saldo * this.taxaRendimento;
        this.saldo += valorRendimento;

        LocalDate dataHoje = LocalDate.now();
        LocalTime horaAgora = LocalTime.now();
        String dataAtual = dataHoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaAtual = horaAgora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transacao transacaoRendimento = new Transacao(dataAtual, horaAtual, valorRendimento, "Rendimento", "ENTRADA", "RENDIMENTO", "CONCLUIDO", null, this);
        this.extrato.add(transacaoRendimento);
    }

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: Sua conta poupança não está ativa.");
        }
        if (destino != null && destino.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");
        }

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na Conta Poupança para realizar o PIX.");
            }
            this.saldo -= valor;

        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) {
                throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Débito.");
            }
            if (cartaoEscolhido.getEstaBloqueado()) {
                throw new LimiteInsuficienteException("Transação recusada: O cartão informado está bloqueado.");
            }
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Débito.");
            }

            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na Conta Poupança para realizar o Débito.");
            }
            this.saldo -= valor;

        } else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
            throw new IllegalArgumentException("Transação recusada: Conta Poupança não possui função Crédito disponível.");

        } else {
            throw new IllegalArgumentException("Método de pagamento inválido para poupança: " + metodoPagamento);
        }

        if (destino != null && (metodoPagamento.equalsIgnoreCase("PIX") || metodoPagamento.equalsIgnoreCase("DEBITO") || metodoPagamento.equalsIgnoreCase("CREDITO"))) {
            destino.saldo += valor;
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

        Transacao transacaoD = transacao;
        this.extrato.add(transacao);
        transacaoD.setTipoFluxo("ENTRADA");
        if (destino != null) {
            destino.extrato.add(transacao);
        }

        return true;
    }

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Taxa de Rendimento: %.2f%%\n", this.taxaRendimento * 100);
    }
}