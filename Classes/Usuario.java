package Classes;

public abstract class Usuario {

    protected int id = 0;
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
        this.id++;
    }

    public void encerrarPerfil() {



    }

    public void alterarDados(String tipoDado){

        if ("Nome".equals(tipoDado)) {
            setNome(nome);

        } else if ("CPF".equals(tipoDado)) {
            setCpf(cpf);

        } else if ("Email".equals(tipoDado)) {
            setEmail(email);

        } else if ("Senha".equals(tipoDado)) {
            setSenha(senha);

        } else if ("Telefone".equals(tipoDado)) {
            setTelefone(telefone);

        }

    }

    public boolean autenticar() {
        return false;
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

    public String visualizarDados(){
        return "\nUSUÁRIO: " + nome +
                "\nCPF: " + cpf +
                "\nE-MAIL: " + email +
                "\nSENHA: ********" +
                "\nDATA DE NASCIMENTO: " + dataNascimento +
                "\nTELEFONE: " + telefone +
                "\nTIPO DO USUÁRIO: " + tipoUsuario;
    }
}