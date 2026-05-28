package Classes;

public class ContaKids extends Conta{

    private String cpfResponsavel;
    private double limiteMensal;

    ContaKids(String numeroConta, String agencia, double saldo, String tipoConta,
              String statusConta, String cpfResponsavel, double limiteMensal) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.cpfResponsavel = cpfResponsavel;
        this.limiteMensal = limiteMensal;
    }

}