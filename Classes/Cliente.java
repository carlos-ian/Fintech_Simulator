package Classes;

import java.util.ArrayList;

public class Cliente extends Usuario {
    ArrayList<Conta> listaContas = new ArrayList<>();

    private Status statusCliente;

    public Cliente(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, Status statusCliente) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.statusCliente = statusCliente;
    }

    public void abrirConta(String numeroConta, String agencia, double saldo, String tipoConta, String statusConta, double limiteChequeEspecial, double taxaRendimento, String cpfResponsavel, double limiteMensal, String perfilRisco) {

        if (tipoConta == "Conta Corrente") {

            ContaCorrente contaCorrente = new ContaCorrente(numeroConta, agencia, saldo, tipoConta, statusConta, limiteChequeEspecial);

            listaContas.add(contaCorrente);

        } else if (tipoConta == "Conta Poupança") {

            ContaPoupanca contaPoupanca = new ContaPoupanca(numeroConta, agencia, saldo, tipoConta,
                    statusConta, taxaRendimento);

            listaContas.add(contaPoupanca);

        } else if (tipoConta == "Conta Kids") {

            ContaKids contaKids = new ContaKids(numeroConta, agencia, saldo, tipoConta,
                    statusConta, cpfResponsavel, limiteMensal);

            listaContas.add(contaKids);

        } else if (tipoConta == "Conta Investimento") {

            ContaInvestimento contaInvestimento = new ContaInvestimento(numeroConta, agencia, saldo, tipoConta,
                    statusConta, perfilRisco);

            listaContas.add(contaInvestimento);

        }

    }

    public boolean fecharConta(Conta conta) {
        for (int i = 0; i < listaContas.size(); i++) {
            if (listaContas.get(i) == conta) {

                listaContas.remove(conta);
            }
        }
    return true;
    }
}