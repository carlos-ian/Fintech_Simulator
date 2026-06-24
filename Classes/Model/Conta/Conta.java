package Classes.Model.Conta;

import Classes.Exceptions.ContaInativaException;
import Classes.Exceptions.LimiteInsuficienteException;
import Classes.Exceptions.SaldoInsuficienteException;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Investimento;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

    /**
     * Classe abstrata responsável por representar uma conta bancária do sistema.
     * <p>Atributos e comportamentos básicos como saldo, agência, número da conta,
     * extrato de transações, cartões vinculados e investimentos realizados.
     * Implementando regras específicas para cada tipo de conta.<p/>
     *
     * @author Ian Carlos
     * @version 1.0
     *
     * @since 2026
     */

public abstract class Conta {
    protected ArrayList<Transacao> extrato = new ArrayList<>();
    protected ArrayList<Cartao> cartoes = new ArrayList<>();
    protected ArrayList<Investimento> listaInvestimentos = new ArrayList<>();

    private static int codigoSequencial = 0;

    protected int id;
    protected String numeroConta;
    protected String agencia;
    protected double saldo;
    protected String tipoConta;
    protected Status statusConta;


    /**
     * Cria uma nova conta bancária.
     *
     * @param numeroConta Número identificador da conta.
     * @param agencia Agência responsável pela conta.
     * @param saldo Saldo inicial.
     * @param tipoConta Tipo da conta criada.
     */

    Conta(String numeroConta, String agencia, double saldo, String tipoConta) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.statusConta = Status.ATIVO;
        this.id = codigoSequencial++;
    }

    /**
     * Realiza uma operação financeira, próprias para processar transações.
     * <p>Esse é um método abstrato que servirá de base para formulação dos métodos
     * de transação das classes que se estendem de conta.<p/>
     *
     * @param valor Valor da operação.
     * @param metodoPagamento Método utilizado para realizar o pagamento.
     * @param cartaoEscolhido Cartão utilizado na operação.
     * @param category Categoria da movimentação financeira.
     * @param destino Conta de destino da transferência.
     * @return true caso a operação seja concluída com sucesso.
     *
     * @throws ContaInativaException Quando a conta estiver inativa.
     * @throws SaldoInsuficienteException Quando o saldo for insuficiente.
     * @throws LimiteInsuficienteException Quando o limite disponível for insuficiente.
     */

    public abstract boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String category, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException;


    /**
     * Método que implementa o RF16: Visualização de Extratos.
     *
     * <p>Consulta o extrato da conta aplicando filtros opcionais.</p>
     *
     * <p>A partir dos parâmetros recebidos, realiza uma sequência de comparações,
     * de modo que ao final do processo, apenas os extratos correspondentes aos
     * filtros selecionados estejam visíveis.</p>
     *
     * <ul>
     *     <li>Fluxo financeiro (Entrada ou Saída);</li>
     *     <li>Método de pagamento;</li>
     *     <li>Categoria;</li>
     *     <li>Quantidade de dias;</li>
     *     <li>Data específica.</li>
     *<ul/>
     *
     * @param fluxo Tipo de fluxo financeiro.
     * @param metodoPagamento Método utilizado na transação.
     * @param categoria Categoria da transação.
     * @param diasPeriodo Período em dias para consulta.
     * @param dataEspecifica Data específica da pesquisa.
     *
     * @return Lista contendo apenas as transações que atendem aos filtros.
     */

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


    /**
     * Método que implementa o RF06: Visualização de Dados da Conta.
     *
     * <p>Exibe as informações básicas da conta.</p>
     *
     * @return String formatada contendo agência, número, tipo,
     * status e saldo da conta.
     */

    public String visualizarDadosConta() {
        return String.format(
                "===================================\n" +
                        "Agência: %s\n" +
                        "Número: %s\n" +
                        "Tipo: %s\n" +
                        "Status: %s\n" +
                        "Saldo: R$ %.2f\n",
                this.agencia, this.numeroConta, this.tipoConta, this.statusConta, this.saldo
        );
    }

    /**  ==========================
     *       GETTERS E SETTERS
     *   ========================== */

    public double getSaldo() {return saldo;}
    public String getAgencia() {return agencia;}
    public String getNumeroConta() {return numeroConta;}
    public void setSaldo(double saldo) {this.saldo = saldo;}
    public ArrayList<Cartao> getCartoes() {return cartoes;}
    public String getTipoConta() {return tipoConta;}
    public Status getStatus() {return statusConta;}
    public void setStatus(Status status) {this.statusConta = status;}
    public ArrayList<Investimento> getListaInvestimentos() {return listaInvestimentos;}
    public ArrayList<Transacao> getExtrato() {return extrato;}
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}
}