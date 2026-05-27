public class ContaPoupanca extends Conta{

    private double taxaRendimento;

    ContaPoupanca(String numeroConta, String agencia, double saldo, String tipoConta,
                  String statusConta, double taxaRendimento) {
        super(numeroConta, agencia, saldo, tipoConta, statusConta);
        this.taxaRendimento = taxaRendimento;
    }

    public void aplicarRendimento(){

    }

}
