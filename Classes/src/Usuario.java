public abstract class Usuario {

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
    }

    public void encerrarPerfil() {
    }

    public void alterarDados(){
    }

    public boolean autenticar() {
        return false;
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
