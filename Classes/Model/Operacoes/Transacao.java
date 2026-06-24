package Classes.Model.Operacoes;

import Classes.Model.Conta.Conta;

    /**
     * Representa uma transação financeira realizada no sistema.
     *
     * <p>Uma transação registra a movimentação de valores entre contas,
     * armazenando informações como data, horário, valor, categoria,
     * método de pagamento, tipo de fluxo e status da operação.</p>
     *
     * <p>As transações são utilizadas para compor o extrato das contas
     * e permitir consultas ao histórico financeiro dos usuários.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class Transacao {
    private static int codigoSequencial = 0;

    private int id;
    private String data;
    private String hora;
    private double valor;
    private String categoria;
    private String tipoFluxo;
    private String metodoPagamento;
    private String status;
    private Conta origem;
    private Conta destino;

    /**
     * Cria uma nova transação financeira.
     *
     * @param data Data da transação.
     * @param hora Horário da transação.
     * @param valor Valor movimentado.
     * @param categoria Categoria da transação.
     * @param tipoFluxo Tipo de fluxo financeiro (Entrada ou Saída).
     * @param metodoPagamento Método de pagamento utilizado.
     * @param status Situação da transação.
     * @param origem Conta de origem da movimentação.
     * @param destino Conta de destino da movimentação.
     */

    public Transacao(String data, String hora, double valor, String categoria, String tipoFluxo, String metodoPagamento, String status, Conta origem, Conta destino) {
        this.id = ++codigoSequencial;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.categoria = categoria;
        this.tipoFluxo = tipoFluxo;
        this.metodoPagamento = metodoPagamento;
        this.status = status;
        this.origem = origem;
        this.destino = destino;
    }

    /**
     *   ======================
     *     GETTERS E SETTERS
     *   ======================
     */

    public  String getData () {return this.data;}
    public  String getCategoria () {return this.categoria;}
    public  String getFluxo () {return this.tipoFluxo;}
    public  String getMetodoPagamento () {return this.metodoPagamento;}
    public  String getStatus () {return this.status;}
    public double getValor () {return this.valor;}
    public String getHora() {return this.hora;}

    public void setTipoFluxo(String tipoFluxo) {this.tipoFluxo = tipoFluxo;}
}
