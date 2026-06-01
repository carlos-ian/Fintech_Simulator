package Classes;

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

    public int getId () {return this.id;}
    public  String getData () {return this.data;}
    public  String getCategoria () {return this.categoria;}
    public  String getFluxo () {return this.tipoFluxo;}
    public  String getMetodoPagamento () {return this.metodoPagamento;}
    public  String getStatus () {return this.status;}
    public  Conta getOrigem () {return this.origem;}
    public  Conta getDestino () {return this.destino;}
    public double getValor () {return this.valor;}
    public String getHora() {return this.hora;}

    public void setTipoFluxo(String tipoFluxo) {this.tipoFluxo = tipoFluxo;}

    public void visualizarTransacao() {
        System.out.printf(this.origem + " --> " + this.destino + "\n" +
                        "[%s %s] %s | %s | %s | Valor: R$ %.2f | Status: %s\n",
                this.data,
                this.hora,
                this.tipoFluxo,
                this.metodoPagamento,
                this.categoria,
                this.valor,
                this.status
        );
    }
}
