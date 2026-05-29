package Classes;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import Classes.Exceptions.*;

public abstract class Conta {
    protected ArrayList<Transacao> extrato;
    protected ArrayList<Cartao> cartoes;
    protected ArrayList<Investimento> listaInvestimentos;
    protected Poupanca poupanca;

    protected String numeroConta;
    protected String agencia;
    protected double saldo;
    protected String tipoConta;
    protected Status statusConta;

    Conta(String numeroConta, String agencia, double saldo, String tipoConta) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.statusConta = Status.ATIVO;
    }

    public abstract boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String category, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException;

    public ArrayList<Transacao> visualizarExtrato(String fluxo, String metodoPagamento, String categoria, Integer diasPeriodo, String dataEspecifica) {
        ArrayList<Transacao> transacoesFiltradas = new ArrayList<>();

        if (this.extrato == null || this.extrato.isEmpty()) {
            return transacoesFiltradas;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoje = LocalDate.now();

        for (Transacao t : this.extrato) {
            boolean filtro = true;

            if (fluxo != null && !fluxo.isEmpty() && !t.getFluxo().equalsIgnoreCase(fluxo)) {
                filtro = false;
            }

            if (filtro && metodoPagamento != null && !metodoPagamento.isEmpty() && !t.getMetodoPagamento().equalsIgnoreCase(metodoPagamento)) {
                filtro = false;
            }

            if (filtro && categoria != null && !categoria.isEmpty() && !t.getCategoria().equalsIgnoreCase(categoria)) {
                filtro = false;
            }

            try {
                if (filtro && diasPeriodo != null && diasPeriodo > 0) {
                    LocalDate dataTransacao = LocalDate.parse(t.getData(), formatter);
                    long diferencaDias = ChronoUnit.DAYS.between(dataTransacao, hoje);

                    if (diferencaDias < 0 || diferencaDias > diasPeriodo) {
                        filtro = false;
                    }
                }

                if (filtro && dataEspecifica != null && !dataEspecifica.isEmpty()) {
                    LocalDate dataTransacao = LocalDate.parse(t.getData(), formatter);
                    LocalDate dataBusca = LocalDate.parse(dataEspecifica, formatter);

                    if (!dataTransacao.isEqual(dataBusca)) {
                        filtro = false;
                    }
                }
            } catch (Exception e) {
                filtro = false;
            }

            if (filtro) {
                transacoesFiltradas.add(t);
            }
        }

        return transacoesFiltradas;
    }

    public String visualizarDadosConta() {
        return String.format(
                "=== DADOS DA CONTA ===\n" +
                        "Agência: %s\n" +
                        "Número: %s\n" +
                        "Tipo: %s\n" +
                        "Status: %s\n" +
                        "Saldo: R$ %.2f\n",
                this.agencia, this.numeroConta, this.tipoConta, this.statusConta, this.saldo
        );
    }


    public double getSaldo() {return saldo;}
    public String getAgencia() {return agencia;}
    public String getNumeroConta() {return numeroConta;}

}