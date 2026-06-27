package Classes.Model.Usuario;

import Classes.Model.Conta.*;

import java.util.ArrayList;

    /**
     * Representa um cliente do sistema bancário.
     *
     * <p>Um cliente é um tipo de usuário que pode possuir uma ou mais contas
     * bancárias, além de realizar operações relacionadas ao gerenciamento
     * dessas contas, como abertura e encerramento.</p>
     *
     * <p>Esta classe herda as funcionalidades básicas da classe
     * {@link Classes.Model.Usuario.Usuario}, adicionando o controle das contas
     * vinculadas ao cliente.</p>
     *
     * @author Davi Mugayar
     * @version 1.0
     * @since 2026
 */

public class Cliente extends Usuario {
    ArrayList<Conta> listaContas = new ArrayList<>();

    /**
     * Cria um novo cliente no sistema.
     *
     * @param nome Nome completo do cliente.
     * @param cpf CPF do cliente.
     * @param email E-mail utilizado para acesso e contato.
     * @param senha Senha criptografada do cliente.
     * @param dataNascimento Data de nascimento.
     * @param telefone Telefone para contato.
     * @param tipoUsuario Tipo do usuário.
     */

    public Cliente(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
    }

    /**
     * Método que implementa o RF08: Alteração de Perfil.
     * <p>Atualiza informações cadastrais do cliente.</p>
     *
     * <p>Sobrescreve o método "alterarDados" da classe Usuário ({@link Classes.Model.Usuario.Usuario}).
     * Por motivos de segurança e integridade dos dados, não é permitido alterar
     * diretamente as contas vinculadas ou o status do cliente através deste método.</p>
     *
     * @param tipoDado Campo que será alterado.
     * @param novoValor Novo valor a ser atribuído.
     *
     * @throws IllegalArgumentException Caso seja realizada
     * uma tentativa de alterar as contas ou o status do cliente.
     */

    @Override
    public void alterarDados(String tipoDado, String novoValor) {
        if ("Contas".equalsIgnoreCase(tipoDado) || "Status".equalsIgnoreCase(tipoDado)) {
            throw new IllegalArgumentException("Não é permitido alterar as contas ou o status do cliente por este método.");
        }
        super.alterarDados(tipoDado, novoValor);
    }

    /**
     * Método que implementa o RF07: Visualização de Perfil.
     * <p>Método que implementa o RF04: Visualização da Lista de Contas.</p>
     *
     * <p>Exibe os dados completos do cliente.</p>
     *
     * <p>Além das informações básicas herdadas da classe
     * {@link Classes.Model.Usuario.Usuario}, também apresenta o status do cliente
     * e as contas bancárias atualmente vinculadas ao perfil.</p>
     *
     * @return String formatada contendo os dados do cliente
     * e as suas respectivas contas.
     */

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

    /**
     * Método que implementa o RF03: Abertura e Encerramento de Contas.
     * <p>Abre uma nova conta bancária para o cliente.</p>
     *
     * <p>O tipo de conta é informado, após isso é determinado qual tipo de classe
     * de Conta será instanciada. Após a criação, a conta é automaticamente adicionada
     * à lista de contas do cliente.</p>
     *
     * <p>Tipos suportados:</p>
     * <ul>
     *     <li>Conta Corrente</li>
     *     <li>Conta Poupança</li>
     *     <li>Conta Kids</li>
     *     <li>Conta Investimento</li>
     * </ul>
     *
     * @param numeroConta Número da nova conta.
     * @param agencia Agência da conta.
     * @param saldo Saldo inicial.
     * @param tipoConta Tipo da conta a ser criada.
     * @param limiteChequeEspecial Limite da conta corrente.
     * @param cpfResponsavel CPF do responsável pela conta kids.
     * @param limiteMensal Limite mensal da conta kids.
     * @param titularCPF Perfil de risco da conta investimento.
     *
     * @return A conta criada ou {@code null} caso o tipo informado
     * seja inválido.
     */

    public Conta abrirConta(String numeroConta, String agencia, double saldo, String tipoConta,
                           double limiteChequeEspecial, String cpfResponsavel,
                           double limiteMensal, String titularCPF) {

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
            ContaInvestimento contaInvestimento = new ContaInvestimento(numeroConta, agencia, saldo, tipoConta, titularCPF);
            listaContas.add(contaInvestimento);
            return contaInvestimento;
        }
        return null;
    }

    /**
     * Método que implementa o RF03: Abertura e Encerramento de Contas.
     * <p>Realiza o encerramento de uma conta vinculada ao cliente.</p>
     *
     * <p>É informada a Conta que o cliente deseja encerrar, após isso se a conta
     * possuir saldo igual a zero, ela poderá ser removida.</p>
     *
     * @param conta Conta que será encerrada.
     *
     * @return {@code true} se a conta foi removida com sucesso;
     * {@code false} caso a conta seja nula ou possua saldo.
     */

    public boolean fecharConta(Conta conta) {
        if (conta == null || conta.getSaldo() != 0) {
            return false;
        }
        return listaContas.remove(conta);
    }

    public ArrayList<Conta> obterContas() {return this.listaContas;}
}