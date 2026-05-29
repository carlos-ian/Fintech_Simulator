package Classes;

import javax.swing.*;
import java.util.ArrayList;
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

    public Usuario (String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.id = cod_sequencial++;
    }

    public static Usuario autenticar(String cpf, String senha) {

        for (Usuario u : AplicacaoBancaria1.ListaUsuarios) {
            if (u.getCpf().equals(cpf)) {
                if(BCrypt.checkpw(senha, u.getSenha())) {
                    return u;
                }
            }
        }
        return null;
    }

    public void encerrarPerfil(Usuario u, String senha) {

        if (!u.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Falha na confirmação: Credenciais inválidas.");
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
        AplicacaoBancaria1.ListaUsuarios.remove(u);
    }

    public void alterarDados(String tipoDado, String novoValor) {

        if ("Nome".equalsIgnoreCase(tipoDado)) {
            this.setNome(novoValor);
        } else if ("CPF".equalsIgnoreCase(tipoDado)) {
            this.setCpf(novoValor);
        } else if ("Email".equalsIgnoreCase(tipoDado)) {
            this.setEmail(novoValor);
        } else if ("Senha".equalsIgnoreCase(tipoDado)) {
            String senhaCriptografada = BCrypt.hashpw(novoValor, BCrypt.gensalt(12));
            this.setSenha(senhaCriptografada);
        } else if ("Telefone".equalsIgnoreCase(tipoDado)) {
            this.setTelefone(novoValor);
        } else if ("DataNascimento".equalsIgnoreCase(tipoDado)) {
            this.setDataNascimento(novoValor);
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



    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTipoUsuario(String tipoUsuario) {
        // Acho que você pode remover os get e set que não tá usando
        this.tipoUsuario = tipoUsuario;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

}