package Classes;

import Classes.Exceptions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaInvestimento extends Conta {
    private String perfilRisco;

    ContaInvestimento(String numeroConta, String agencia, double saldo, String tipoConta, String perfilRisco) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.perfilRisco = perfilRisco;
    }

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException {

        if (this.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: Sua conta investimento não está ativa.");
        }
        if (destino != null && destino.statusConta != Status.ATIVO) {
            throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");
        }

        if (metodoPagamento.equalsIgnoreCase("DEBITO") || metodoPagamento.equalsIgnoreCase("CREDITO")) {
            throw new IllegalArgumentException("Transação recusada: Conta Investimento não suporta compras diretas via cartão. Use uma Conta Corrente.");
        }

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente na Conta Investimento para realizar essa transferência.");
            }
            this.saldo -= valor;
        } else {
            throw new IllegalArgumentException("Método de pagamento inválido para investimentos: " + metodoPagamento);
        }

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

        this.extrato.add(transacao);
        if (destino != null) {
            transacao.setTipoFluxo("ENTRADA");
            destino.extrato.add(transacao);
        }

        return true;
    }

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Perfil de Risco: %s\n", this.perfilRisco);
    }
}
