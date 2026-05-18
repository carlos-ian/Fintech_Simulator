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

    Transacao(String data, String hora, double valor, String categoria, String tipoFluxo, String metodoPagamento, String status, Conta origem, Conta destino) {
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
}
