package Classes;

import java.util.ArrayList;

public class Cliente extends Usuario {
    ArrayList<Conta> listaContas = new ArrayList<>();

    private String statusCliente;

    public Cliente(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String statusCliente) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.statusCliente = statusCliente;
        this.id = id+1;
    }

    /*public void abrirConta() {

        int opcaoConta;
        String tipoConta;

        if (opcaoConta == 1) {

            tipoConta = "Conta Corrente";

            contaCorrente contapoupanca = new contaCorrente();

        } else if (opcaoConta == 2) {
            tipoConta = "Conta Poupança";

            contaPoupanca contapoupanca = new contaPoupanca();

        } else if (opcaoConta == 3) {
            tipoConta = "Conta Kids";

            contaKids contacorrente = new contaKids();

        } else if (opcaoConta == 4) {
            tipoConta = "Conta Investimento";

            contaInvestimento contaInvestimento = new contaInvestimento();

        }

    }
*/
    public boolean fecharConta() {
        return false;
    }

    }
