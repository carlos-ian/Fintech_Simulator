import java.util.ArrayList;

public class Cliente extends Usuario {
    //ArrayList<Conta> listaContas = new ArrayList<>();

    private String statusCliente;

    public Cliente(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String statusCliente) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.statusCliente = statusCliente;
    }

    public void abrirConta() {
    }

    public boolean fecharConta() {
        return false;
    }
    }
