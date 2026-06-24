package Classes.Model.Operacoes;

import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Conta.Conta;

    /**
     * Representa um produto de investimento disponível na fintech.
     *
     * <p>Um investimento possui informações como nome do produto,
     * taxa de rendimento, valor aplicado e data da aplicação.</p>
     *
     * <p>A classe também disponibiliza operações para:</p>
     * <ul>
     *     <li>Aplicar rendimento sobre o valor investido;</li>
     *     <li>Realizar um novo investimento;</li>
     *     <li>Resgatar investimentos existentes;</li>
     *     <li>Consultar informações do produto.</li>
     * </ul>
     *
     * @author Joel Antônio
     * @version 1.0
     * @since 2026
     */

public class Investimento {
    private static int contador = 0;

    private int idInvestimento;
    private String nomeProduto;
    private double taxaRendimento;
    private double valorAplicado;
    private String dataAplicacao;

    /**
     * Cria um novo produto de investimento.
     *
     * @param nomeProduto Nome do produto financeiro.
     * @param taxaRendimento Taxa de rendimento aplicada.
     * @param valorAplicado Valor inicialmente aplicado.
     * @param dataAplicacao Data da aplicação.
     */

    public Investimento(String nomeProduto, double taxaRendimento, double valorAplicado, String dataAplicacao){
        idInvestimento = contador++;
        this.nomeProduto = nomeProduto;
        this.taxaRendimento = taxaRendimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
    }

    /**
     * Aplica o rendimento do investimento sobre o valor investido.
     *
     * <p>O cálculo é realizado multiplicando o valor aplicado
     * pela taxa de rendimento definida para o produto.</p>
     *
     * <p>O valor obtido é incorporado ao montante investido,
     * simulando a valorização da aplicação.</p>
     */

    public void aplicarRendimento() {
        this.valorAplicado += this.valorAplicado * this.taxaRendimento;
    }

    /**
     * Método que implementa o RF17: Realização de Investimentos.
     *
     * <p>Realiza um investimento utilizando o saldo disponível
     * de uma conta.</p>
     *
     * <p>O valor investido é descontado do saldo da conta e
     * registrado na lista de investimentos do cliente.</p>
     *
     * <p>Também são atualizados o valor aplicado e a data
     * da aplicação do produto selecionado.</p>
     *
     * @param conta Conta utilizada para realizar o investimento.
     * @param produtoSelecionado Produto escolhido para aplicação.
     * @param valorParaInvestir Valor a ser investido.
     * @param dataAtual Data da realização da aplicação.
     *
     * @return {@code true} caso o investimento seja realizado
     * com sucesso.
     *
     * @throws SaldoInsuficienteException Caso a conta não possua
     * saldo suficiente para realizar a aplicação.
     *
     * @throws IllegalArgumentException Caso o valor informado
     * seja menor ou igual a zero.
     */

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

    /**
     * Método que implementa o RF18: Resgate de Investimentos.
     *
     * <p>Realiza o resgate de um investimento anteriormente aplicado.</p>
     *
     * <p>O valor investido é devolvido ao saldo da conta e o
     * investimento é removido da lista de investimentos ativos.</p>
     *
     * @param conta Conta que receberá o valor resgatado.
     * @param idInvestimento Identificador do investimento.
     *
     * @return {@code true} caso o resgate seja realizado
     * com sucesso.
     *
     * @throws IllegalArgumentException Caso o investimento
     * informado não seja encontrado.
     *
     * @throws Exception Caso ocorra algum erro durante o processo
     * de resgate.
     */

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

    /**
     * Retorna uma representação textual do investimento.
     *
     * <p>Exibe as principais informações do produto,
     * incluindo identificador, nome, taxa de rendimento,
     * valor aplicado e data da aplicação.</p>
     *
     * @return String contendo os dados do investimento.
     */

    @Override
    public String toString() {

        return "\nID: " + idInvestimento +
                "\nProduto: " + nomeProduto +
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