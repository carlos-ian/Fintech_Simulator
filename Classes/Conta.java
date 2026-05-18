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

    // public boolean realizarTransacao(double valor, Conta origem, Conta destino) {}
    // public boolean receberTransacao(double valor, Conta origem, Conta destino) {}
    // public ArrayList<Transacao> visualizarExtrato(int periodo) {}



}
