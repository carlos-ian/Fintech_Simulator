package Classes;

public class Poupanca {

    private int idReserva;
    private String nomeReserva;
    private double  saldoReservado;
    private String dataCriacao;
    private double valorMeta;
    private double taxaRendimento;

    private static int contador = 1;

    public Poupanca(String nomeReserva,
                    double saldoReservado,
                    String dataCriacao,
                    double valorMeta,
                    double taxaRendimento) {

        this.idReserva = contador++;
        this.nomeReserva = nomeReserva;
        this.saldoReservado = saldoReservado;
        this.dataCriacao = dataCriacao;
        this.valorMeta = valorMeta;
        this.taxaRendimento = taxaRendimento;
    }

    public String visualizarPoupanca() {
        return "\nID Reserva: " + idReserva +
                "\nNome Reserva: " + nomeReserva +
                "\nSaldo Reservado: R$ " + saldoReservado +
                "\nData Criação: " + dataCriacao +
                "\nValor Meta: R$ " + valorMeta +
                "\nTaxa Rendimento: " + taxaRendimento + "%\n";
    }

    public boolean guardar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return false;
        }

        saldoReservado += valor;
        double rendimento = saldoReservado * (taxaRendimento / 100);
        saldoReservado += rendimento;

        System.out.println("Valor guardado com sucesso!");
        System.out.println("Saldo atual: R$ " + saldoReservado);
        return true;
    }

    public boolean retirar(double valor) {

        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return false;
        }

        if (valor > saldoReservado) {
            System.out.println("Saldo insuficiente.");
            return false;
        }

        saldoReservado -= valor;
        System.out.println("Retirada realizada com sucesso!");
        System.out.println("Saldo atual: R$ " + saldoReservado);
        return true;
    }
}