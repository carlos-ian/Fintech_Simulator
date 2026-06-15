package Classes.Model.Operacoes;

import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Conta.Conta;

public class Investimento {
    private static int contador = 0;

    private int idInvestimento;
    private String nomeProduto;
    private double taxaRendimento;
    private double valorAplicado;
    private String dataAplicacao;

    public Investimento(String nomeProduto, double taxaRendimento, double valorAplicado, String dataAplicacao){
        idInvestimento = contador++;
        this.nomeProduto = nomeProduto;
        this.taxaRendimento = taxaRendimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
    }

    public void aplicarRendimento() {this.valorAplicado += this.valorAplicado * (this.taxaRendimento / 100.0);}

    public boolean realizarInvestimento(Conta conta, Investimento produtoSelecionado, double valorParaInvestir, String dataAtual) throws SaldoInsuficienteException {

        if (valorParaInvestir <= 0) {
            throw new IllegalArgumentException("O valor do investimento deve ser maior que zero.");
        }

        if (valorParaInvestir > conta.getSaldo()) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar este investimento.");
        }

        conta.setSaldo(conta.getSaldo() - valorParaInvestir);
        produtoSelecionado.dataAplicacao = dataAtual;
        produtoSelecionado.valorAplicado = valorParaInvestir;

        conta.getListaInvestimentos().add(produtoSelecionado);

        System.out.printf("Investimento de R$ %.2f em '%s' realizado com sucesso!\n",
                valorParaInvestir, produtoSelecionado.nomeProduto);
        return true;
    }

    public boolean resgatarInvestimento(Conta conta, int idInvestimento) throws Exception {
        Investimento investimentoEncontrado = null;

        for (Investimento inv : conta.getListaInvestimentos()) {
            if (inv.idInvestimento == idInvestimento) {
                investimentoEncontrado = inv;
                break;
            }
        }

        if (investimentoEncontrado == null) {
            throw new IllegalArgumentException("Investimento com o ID " + idInvestimento + " não foi encontrado.");
        }

        double valorParaDevolver = investimentoEncontrado.valorAplicado;

        conta.setSaldo(conta.getSaldo() + valorParaDevolver);
        conta.getListaInvestimentos().remove(investimentoEncontrado);

        System.out.printf("Resgate de R$ %.2f do produto '%s' realizado com sucesso!\n",
                valorParaDevolver, investimentoEncontrado.nomeProduto);
        return true;
    }

    @Override
    public String toString() {

        return "\nProduto: " + nomeProduto +
                "\nTaxa de rendimento: " + taxaRendimento + "%" +
                "\nValor aplicado: R$ " + valorAplicado +
                "\nData aplicação: " + dataAplicacao + "\n";
    }

    public String getNomeProduto() {return nomeProduto;}
    public double getTaxaRendimento() {return taxaRendimento;}
    public double  getValorAplicado() {return valorAplicado;}
    public void setId(int id) {this.idInvestimento = id;}
    public int getId() {return idInvestimento;}
}