package Classes.Model.Usuario;

import Classes.Model.Conta.*;

import java.util.ArrayList;

public class Cliente extends Usuario {
    ArrayList<Conta> listaContas = new ArrayList<>();

    public Cliente(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
    }

    @Override
    public void alterarDados(String tipoDado, String novoValor) {
        if ("Contas".equalsIgnoreCase(tipoDado) || "Status".equalsIgnoreCase(tipoDado)) {
            throw new IllegalArgumentException("Não é permitido alterar as contas ou o status do cliente por este método.");
        }
        super.alterarDados(tipoDado, novoValor);
    }

    @Override
    public String visualizarDados() {
        String dadosBasicos = super.visualizarDados();

        String dadosContas = "";
        if (this.listaContas == null || this.listaContas.isEmpty()) {
            dadosContas = "\nCONTAS: Nenhuma conta vinculada.";
        } else {
            dadosContas = "\nCONTAS ATIVAS:";
            for (Conta c : this.listaContas) {
                dadosContas += "\n  - Agência: " + c.getAgencia() + " | Número: " + c.getNumeroConta() + " | Saldo: R$ " + c.getSaldo();
            }
        }
        return dadosBasicos +
                "\nSTATUS DO CLIENTE: " + this.statusPerfil + dadosContas;
    }

    public Conta abrirConta(String numeroConta, String agencia, double saldo, String tipoConta,
                           double limiteChequeEspecial, String cpfResponsavel,
                           double limiteMensal, String perfilRisco) {

        if (tipoConta.equalsIgnoreCase("Conta Corrente")) {
            ContaCorrente contaCorrente = new ContaCorrente(numeroConta, agencia, saldo, tipoConta, limiteChequeEspecial);
            listaContas.add(contaCorrente);
            return contaCorrente;

        } else if (tipoConta.equalsIgnoreCase("Conta Poupança")) {
            ContaPoupanca contaPoupanca = new ContaPoupanca(numeroConta, agencia, saldo, tipoConta);
            listaContas.add(contaPoupanca);
            return contaPoupanca;

        } else if (tipoConta.equalsIgnoreCase("Conta Kids")) {
            ContaKids contaKids = new ContaKids(numeroConta, agencia, saldo, tipoConta, cpfResponsavel, limiteMensal);
            listaContas.add(contaKids);
            return contaKids;

        } else if (tipoConta.equalsIgnoreCase("Conta Investimento")) {
            ContaInvestimento contaInvestimento = new ContaInvestimento(numeroConta, agencia, saldo, tipoConta, perfilRisco);
            listaContas.add(contaInvestimento);
            return contaInvestimento;
        }
        return null;
    }

    public boolean fecharConta(Conta conta) {
        if (conta == null || conta.getSaldo() != 0) {
            return false;
        }
        return listaContas.remove(conta);
    }

    public ArrayList<Conta> obterContas() {return this.listaContas;}
}