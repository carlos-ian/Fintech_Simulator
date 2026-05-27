package Classes;

public class ContaInvestimento extends Conta{

    private String perfilRisco;

    ContaInvestimento(String numeroConta, String agencia, double saldo, String tipoConta,
                      String statusConta, String perfilRisco) {
        super(numeroConta, agencia, saldo, tipoConta, statusConta);
        this.perfilRisco = perfilRisco;
    }
}
