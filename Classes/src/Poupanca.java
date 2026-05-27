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

    // ==================================================
    // RF14 - VISUALIZAR POUPANÇA
    // ==================================================

    public double visualizarPoupanca() {

        return saldoReservado;
    }

    // ==================================================
    // RF15 - GUARDAR DINHEIRO
    // ==================================================

    public boolean guardar(double valor) {

        // validação
        if (valor <= 0) {

            System.out.println("Valor inválido.");
            return false;
        }

        // adiciona saldo
        saldoReservado += valor;

        // aplica rendimento
        double rendimento =
                saldoReservado * (taxaRendimento / 100);

        saldoReservado += rendimento;

        System.out.println("Valor guardado com sucesso!");
        System.out.println("Saldo atual: R$ "
                + saldoReservado);

        return true;
    }

    // ==================================================
    // RF15 - RETIRAR DINHEIRO
    // ==================================================

    public boolean retirar(double valor) {

        // validação
        if (valor <= 0) {

            System.out.println("Valor inválido.");
            return false;
        }

        // verifica saldo
        if (valor > saldoReservado) {

            System.out.println("Saldo insuficiente.");
            return false;
        }

        // retira valor
        saldoReservado -= valor;

        System.out.println("Retirada realizada com sucesso!");
        System.out.println("Saldo atual: R$ "
                + saldoReservado);

        return true;
    }

    // ==================================================
    // GETTERS E SETTERS
    // ==================================================

    public int getIdReserva() {return idReserva;}

    public String getNomeReserva() {return nomeReserva;}
    public void setNomeReserva(String nomeReserva) {this.nomeReserva = nomeReserva;}

    public double getSaldoReservado() {return saldoReservado;}
    public void setSaldoReservado(double saldoReservado) {this.saldoReservado = saldoReservado;}

    public String getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(String dataCriacao) {this.dataCriacao = dataCriacao;}

    public double getValorMeta() {return valorMeta;}
    public void setValorMeta(double valorMeta) {this.valorMeta = valorMeta;}

    public double getTaxaRendimento() {return taxaRendimento;}
    public void setTaxaRendimento(double taxaRendimento) {this.taxaRendimento = taxaRendimento;}

    // ==================================================
    // TO STRING
    // ==================================================

    @Override
    public String toString() {

        return "\nID Reserva: " + idReserva +
                "\nNome Reserva: " + nomeReserva +
                "\nSaldo Reservado: R$ " + saldoReservado +
                "\nData Criação: " + dataCriacao +
                "\nValor Meta: R$ " + valorMeta +
                "\nTaxa Rendimento: " + taxaRendimento + "%\n";
    }

}