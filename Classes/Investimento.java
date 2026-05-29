package Classes;

import Classes.Exceptions.*;
import java.util.ArrayList;

public class Investimento {
    private static int contador = 0;

    private int idInvestimento;
    private String nomeProduto;
    private double taxaRendimento;
    private double valorAplicado;
    private String dataAplicacao;
    private String Status;

    Investimento(String nomeProduto, double taxaRendimento, double valorAplicado, String dataAplicacao,
                 String Status){
        idInvestimento = contador++;
        this.nomeProduto = nomeProduto;
        this.taxaRendimento = taxaRendimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
        this.Status = Status;
    }

    public ArrayList<Investimento> visualizarInvestimentosFeitos(Conta conta) {
        if (conta.listaInvestimentos == null || conta.listaInvestimentos.isEmpty()) {
            return null;
        }
        return conta.listaInvestimentos;
    }

    public static ArrayList<Investimento> visualizarInvestimentosDisponiveis() {
        if (AplicacaoBancaria.produtosDisponiveis == null || AplicacaoBancaria.produtosDisponiveis.isEmpty()) {
            return null;
        }
        return AplicacaoBancaria.produtosDisponiveis;
    }

    public boolean realizarInvestimento(Conta conta, Investimento produtoSelecionado, double valorParaInvestir, String dataAtual) throws SaldoInsuficienteException {

        if (valorParaInvestir <= 0) {
            throw new IllegalArgumentException("O valor do investimento deve ser maior que zero.");
        }

        if (valorParaInvestir > conta.saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar este investimento.");
        }

        conta.saldo -= valorParaInvestir;

        Investimento novoInvestimento = new Investimento(
                produtoSelecionado.nomeProduto,
                produtoSelecionado.taxaRendimento,
                valorParaInvestir,
                dataAtual,
                "Ativo"
        );

        conta.listaInvestimentos.add(novoInvestimento);

        System.out.printf("Investimento de R$ %.2f em '%s' realizado com sucesso!\n",
                valorParaInvestir, produtoSelecionado.nomeProduto);
        return true;
    }

    public boolean resgatarInvestimento(Conta conta, int idInvestimento) throws Exception {
        Investimento investimentoEncontrado = null;

        for (Investimento inv : conta.listaInvestimentos) {
            if (inv.idInvestimento == idInvestimento) {
                investimentoEncontrado = inv;
                break;
            }
        }

        if (investimentoEncontrado == null) {
            throw new IllegalArgumentException("Investimento com o ID " + idInvestimento + " não foi encontrado.");
        }

        if (investimentoEncontrado.Status.equalsIgnoreCase("Resgatado")) {
            throw new IllegalStateException("Este investimento já foi resgatado anteriormente.");
        }

        double valorParaDevolver = investimentoEncontrado.valorAplicado;
        conta.saldo += valorParaDevolver;

        investimentoEncontrado.Status = "Resgatado";

        System.out.printf("Resgate de R$ %.2f do produto '%s' realizado com sucesso!\n",
                valorParaDevolver, investimentoEncontrado.nomeProduto);
        return true;
    }

    @Override
    public String toString() {

        return "\nID: " + idInvestimento +
                "\nProduto: " + nomeProduto +
                "\nTaxa de rendimento: " + taxaRendimento + "%" +
                "\nValor aplicado: R$ " + valorAplicado +
                "\nData aplicação: " + dataAplicacao +
                "\nStatus: " + Status + "\n";
    }
}