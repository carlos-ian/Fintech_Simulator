import java.util.ArrayList;
import java.util.List;

public class Investimento {

    private int idInvestimento;
    private String nomeProduto;
    private double taxaRendimento;
    private double valorAplicado;
    private String dataAplicacao;
    private String Status;
    /* Possíveis atributos "descrição." */

    // LISTA DE INVESTIMENTOS DISPONÍVEIS
    private static List<Investimento> listaInvestimentos = new ArrayList<>();

    private static int contador = 0;

    Investimento(String nomeProduto, double taxaRendimento, double valorAplicado, String dataAplicacao,
                 String Status){

        idInvestimento = contador++;
        this.nomeProduto = nomeProduto;
        this.taxaRendimento = taxaRendimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
        this.Status = Status;
    }

    // ==================================================
    // RF11 - VISUALIZAR INVESTIMENTOS
    // ==================================================

    public static List<Investimento> visualizarInvestimentos() {

        return listaInvestimentos;
    }

    // ==================================================
    // RF12 - REALIZAR INVESTIMENTO
    // ==================================================

    public boolean realizarInvestimento(double valor,
                                        String produto) {

        // verifica se o produto existe
        if (!this.nomeProduto.equalsIgnoreCase(produto)) {

            System.out.println("Produto não encontrado.");
            return false;
        }

        // adiciona o valor aplicado
        this.valorAplicado += valor;

        // simulação de rendimento
        double rendimento =
                valorAplicado * (taxaRendimento / 100);

        this.valorAplicado += rendimento;

        this.Status = "ATIVO";

        System.out.println("Investimento realizado com sucesso!");
        System.out.println("Valor total com rendimento: R$ "
                + this.valorAplicado);

        return true;
    }

    // ==================================================
    // RF13 - RESGATAR INVESTIMENTO
    // ==================================================

    public double resgatarInvestimento(int idInvestimento) {

        if (this.idInvestimento != idInvestimento) {

            System.out.println("Investimento não encontrado.");
            return 0;
        }

        if (this.Status.equalsIgnoreCase("RESGATADO")) {

            System.out.println("Investimento já resgatado.");
            return 0;
        }

        this.Status = "RESGATADO";

        double valorResgatado = this.valorAplicado;

        this.valorAplicado = 0;

        System.out.println("Investimento resgatado com sucesso!");

        return valorResgatado;
    }

    // ==================================================
    // MÉTODO AUXILIAR
    // ==================================================

    public static void adicionarInvestimento(
            Investimento investimento) {

        listaInvestimentos.add(investimento);
    }

    // ==================================================
    // GETTERS E SETTERS
    // ==================================================

    public int getIdInvestimento() {return idInvestimento;}

    public String getNomeProduto() {return nomeProduto;}
    public void setNomeProduto(String nomeProduto) {this.nomeProduto = nomeProduto;}

    public double getTaxaRendimento() {return taxaRendimento;}
    public void setTaxaRendimento(double taxaRendimento) {this.taxaRendimento = taxaRendimento;}

    public double getValorAplicado() {return valorAplicado;}
    public void setValorAplicado(double valorAplicado) {this.valorAplicado = valorAplicado;}

    public String getDataAplicacao() {return dataAplicacao;}
    public void setDataAplicacao(String dataAplicacao) {this.dataAplicacao = dataAplicacao;}

    public String getStatus() {return Status;}
    public void setStatus(String Status) {this.Status = Status;}

    // ==================================================
    // TO STRING
    // ==================================================

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