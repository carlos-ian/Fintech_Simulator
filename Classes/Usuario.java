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
        this.id++; // Não esquece de corrigir o código e o id, man
    }

    public boolean autenticar() { // Parâmetros: Email/CPF (Identificador) e senha descriptograda
        // 1. Busca Usuário na lista de Usuários da Aplicação Bancária baseado no identificador (CPF/Email)
        // Se quiser, divide em lista de cliente e lista de administradores, mas você que sabe

        // 2. Se o Usuário foi encontrado, verifica se a senha enviada é igual a senha criptograda do usuário
        // encontrado
        // if (BCrypt.checkpw(senha enviada, senha criptografada do usuário)) {return true}

        // Retorna false, se não encontrar usuário ou senha estiver diferente

        // OBS: Se quiser, pode fazer esse processo verificando primeiro na lista de clientes
        // E depois repitir o código com a lista de admnistradores.

        // OBS: Não esqueça do import org.mindrot.jbcrypt.BCrypt;

        return false;
    }

    public void encerrarPerfil() { // Parâmetros: Perfil do Usuário e talvez, CPF/Email e senha
        // 1. Antes de encerrar ou excluir o perfil do usuário, verifique se ele é cliente ou não
        // Se for cliente, só poderá excluir se o saldo de todas suas contas = 0

        // 2. Após essa verificação, implemente uma forma de confirmação da exclusão
        // Ou seja, utilize o cpf/email e a senha do que o usuário deve passar para confirmar que ele
        // quer excluir, verificando se essas credenciais é de seu perfil mesmo
        // Entretanto, se quiser, pode apenas fazer uma confirmação simples sem CPF/Email e senha
        // O importante é ter a confirmação de alguma forma

        // 3. Por fim, remove o usuário da lista de usuários da aplicação
    }

    public void desativarAtivarPerfil() {
        // 1. Muda o status do perfil do usuário para inativo ou ativo
        // 2. Muda o status das contas do usuário, se for cliente, para inativo ou ativo

        // Pense em como você quer representar esses estados, ou seja, quais restrições o
        // usuário vai ter com o perfil desativado
        // A minha ideia é que ele pudesse logar e ver seus dados de perfil normalmente
        // Mas não poderia realizar nada além de suas configurações de perfil
    } // Isso é apenas uma ideia, se quiser pular essa parte por enquanto, fale comigo

    public void alterarDados(String tipoDado){ // Parâmetros: Tipo de Dado e Novo Valor
        // A forma como você fez está certa, mas acho que poderia adicionar data de nascimento também
        // Pode ser que a pessoa acabe colocando a data errada ou sla
        if ("Nome".equals(tipoDado)) {
            setNome(nome); // em vez de passar nome, passe o Novo Valor setNome(novoValor) por exemplo

        } else if ("CPF".equals(tipoDado)) {
            setCpf(cpf);

        } else if ("Email".equals(tipoDado)) {
            setEmail(email);

        } else if ("Senha".equals(tipoDado)) {
            setSenha(senha); // Não esquece de criptografar a nova senha, nesse caso

        } else if ("Telefone".equals(tipoDado)) {
            setTelefone(telefone);

        }
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

    public String visualizarDados(){ // Se quiser, já printa aqui em vez de retornar a string
        return "\nUSUÁRIO: " + nome +
                "\nCPF: " + cpf +
                "\nE-MAIL: " + email +
                "\nSENHA: ********" + // Pode mostrar a os primeiros digitos se quiser
                "\nDATA DE NASCIMENTO: " + dataNascimento +
                "\nTELEFONE: " + telefone +
                "\nTIPO DO USUÁRIO: " + tipoUsuario; // Pode tirar esse aqui
    } // Você tem duas formas de fazer esse de visualizar os Dados:
    // 1. Polimorfismo: Usuário tem esse metodo de visualizar os dados já implementado (com esse código aí)
    // Os filhos de usuário (Cliente e Administrador), vai fazer a sobrescrita (override) desse metodo
    // chamando o super e adicionando os seus atributos depois

    // 2. Metodo Abstrato: Coloque esse metodo com abstrato e não coloque nenhum codigo dentro
    // Os filhos vão implementa-lo obrigatoriamente mostrando todos seus dados
}