package Classes.Model.Usuario;

import Classes.AplicacaoBancaria;
import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Status;
import org.mindrot.jbcrypt.BCrypt;

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

    public String visualizarDados() {
        return "\n=== DADOS DO PERFIL ===" +
                "\nUSUÁRIO: " + this.nome +
                "\nCPF: " + this.cpf +
                "\nE-MAIL: " + this.email +
                "\nSENHA: **********" +
                "\nDATA DE NASCIMENTO: " + this.dataNascimento +
                "\nTELEFONE: " + this.telefone;
    }

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