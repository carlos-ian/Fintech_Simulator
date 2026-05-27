public class ContaCorrente extends Conta{

    private double limiteChequeEspecial;

    ContaCorrente(String numeroConta, String agencia, double saldo, String tipoConta,
                  String statusConta, double limiteChequeEspecial) {
        super(numeroConta, agencia, saldo, tipoConta, statusConta);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    public void solicitarAumentoLimite(){

    }

}
