package Classes.Model.Usuario;

import Classes.AplicacaoBancaria;
import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Status;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe abstrata que representa um usuário do sistema bancário.

 * <p>Esta classe serve como base para os diferentes tipos de usuários
 * da aplicação, como Cliente e Administrador, armazenando informações
 * cadastrais e disponibilizando operações comuns de autenticação,
 * gerenciamento e visualização de perfil.</p>
 *
 * @author Davi Mugayar
 * @version 1.0
 * @since 2026
 */

public abstract class Usuario {
    private static int cod_sequencial = 0;

    protected int id;
    protected String nome;
    protected String cpf;
    protected String email;
    protected String senha;
    protected String dataNascimento;
    protected String telefone;
    protected String tipoUsuario;
    protected Status statusPerfil;

    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome Nome completo do usuário.
     * @param cpf CPF do usuário.
     * @param email E-mail do usuário.
     * @param senha Senha criptografada do usuário.
     * @param dataNascimento Data de nascimento.
     * @param telefone Telefone para contato.
     * @param tipoUsuario Tipo de usuário.
     */

    public Usuario (String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.statusPerfil = Status.ATIVO;
        this.id = cod_sequencial++;
    }

    /**
     * Método que implementa o RF02: Autenticação.
     * <p>Realiza a autenticação de um usuário no sistema.</p>
     *
     * <p>A autenticação é realizada a partir de uma busca utilizando o CPF
     * informado e,em seguida, a senha fornecida é comparada com a senha criptografada
     * armazenada utilizando BCrypt.</p>
     *
     * @param cpf CPF utilizado para login.
     * @param senha Senha informada pelo usuário.
     *
     * @return O usuário autenticado caso as credenciais sejam válidas;
     * retorna {@code null} caso a autenticação falhe.
     */

    public static Usuario autenticar(String cpf, String senha) {
        for (Usuario u : AplicacaoBancaria.listaUsuarios) {
            if (u.getCpf().equals(cpf)) {
                if (BCrypt.checkpw(senha, u.getSenha())) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Método que implementa o RF10: Exclusão de Perfil.
     * <p>Remove um perfil do sistema.</p>
     *
     * <p>Antes de realizar a remoção, confere se a senha informada é valida,
     * caso o usuário seja um cliente, também é verificado se todas as contas
     * vinculadas possuem saldo zerado. Caso todas as condições sejam cumpridas
     * o perfil e encerrado com sucesso.</p>
     *
     * @param u Usuário que terá o perfil encerrado.
     * @param senha Senha de confirmação.
     *
     * @throws IllegalArgumentException Caso a senha esteja incorreta.
     * @throws IllegalStateException Caso existam contas com saldo disponível.
     */

    public void encerrarPerfil(Usuario u, String senha) {

        if (!BCrypt.checkpw(senha, u.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta! Não foi possível excluir o perfil.");
        }

        if (u instanceof Cliente) {
            Cliente cliente = (Cliente) u;
            double saldoTotal = 0;

            for (Conta conta : cliente.listaContas) {
                saldoTotal += conta.getSaldo();
            }

            if (saldoTotal != 0) {
                throw new IllegalStateException("Não é possível encerrar: o cliente possui saldo em conta.");
            }
        }
        AplicacaoBancaria.listaUsuarios.remove(u);
    }

    /**
     * Método que implementa o RF08: Alteração de Perfil.
     * <p>Atualiza um dado cadastral do usuário.</p>
     * <p>Realiza a alteração de dados do usuário a partir do tipo de dado
     * ou do novo valor de intersse, após informado esses campos, é realizada
     * uma verificação de qual dado será alterado.É Permitido alterar nome,
     * CPF, e-mail, senha, telefone e data de nascimento.</p>
     *
     * <p>Quando a senha é alterada, ela é automaticamente
     * criptografada utilizando BCrypt antes de ser armazenada.</p>
     *
     * @param tipoDado Campo que será alterado.
     * @param novoValor Novo valor a ser atribuído.
     *
     * @throws IllegalArgumentException Caso o campo informado seja inválido.
     */

    public void alterarDados(String tipoDado, String novoValor) {
        if ("Nome".equalsIgnoreCase(tipoDado)) {
            this.nome = novoValor;
        } else if ("CPF".equalsIgnoreCase(tipoDado)) {
            this.cpf = novoValor;
        } else if ("Email".equalsIgnoreCase(tipoDado)) {
            this.email = novoValor;
        } else if ("Senha".equalsIgnoreCase(tipoDado)) {
            this.senha = BCrypt.hashpw(novoValor, BCrypt.gensalt());
        } else if ("Telefone".equalsIgnoreCase(tipoDado)) {
            this.telefone = novoValor;
        } else if ("DataNascimento".equalsIgnoreCase(tipoDado)) {
            this.dataNascimento = novoValor;
        } else {
            throw new IllegalArgumentException("Tipo de dado inválido");
        }
    }

    /**
     * Método que implementa o RF07: Visualização de Perfil.
     * Exibe os dados cadastrais do usuário.
     *
     * <p>Por questões de segurança, a senha não é exibida
     * em texto puro.</p>
     *
     * @return String formatada contendo as informações do perfil.
     */

    public String visualizarDados() {
        return "\n=== DADOS DO PERFIL ===" +
                "\nUSUÁRIO: " + this.nome +
                "\nCPF: " + this.cpf +
                "\nE-MAIL: " + this.email +
                "\nSENHA: **********" +
                "\nDATA DE NASCIMENTO: " + this.dataNascimento +
                "\nTELEFONE: " + this.telefone;
    }

    /** ==========================
     *     GETTERS E SETTERS
     *  ========================== */

    public String getTipoUsuario() {return tipoUsuario;}
    public String getNome() {
        return nome;
    }
    public String getSenha() {
        return senha;
    }
    public String getCpf() {
        return cpf;
    }
    public Status getStatus() {return this.statusPerfil;}
    public void setStatus(Status statusCliente) {this.statusPerfil = statusCliente;}

    public String getEmail() {return email;}
    public String getDataNascimento() {return dataNascimento;}
    public String getTelefone() {return telefone;}
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

}