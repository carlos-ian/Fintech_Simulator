package Classes;

public class AplicacaoBancaria1 {
    public static void main(String[] args) {

        // Opções:
        // 1. Fazer Login
        // 2. Criar Nova Conta de Usuário
        // 3. Sair

        // Se for opção 1, mostre a opção de Login como Cliente, Login como Admnistrador e Voltar
        // O Login deve ser feito usando Email/CPF (escolha um) e senha
        // Chame o metodo de autenticar para validar e direcione para o menu correspodente ao usuário
        // se a validação for true

        // Se for opção 2, mostra a opção de Criar Conta como Cliente, Criar Conta como Administrador e Voltar
        // Criar conta deve ter os campos relacionados as informações do perfil de usuário
        // Para o administrador, adicione um campo em que deve ser enviado a matrícula para validar se
        // ele pode ser realmente criado
        // A senha deve ser salva de forma criptografa, para isso no construtor do Usuário ou no menu antes
        // de enviar a senha, faça: senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
        // No final, enviei as informações coletadas para o construtor e adicione o usuário a lista da Aplicação

        // OBS: Não esqueça do import org.mindrot.jbcrypt.BCrypt;

    } // Menu de Entrada -> Autenticação e Criação de Perfil

    public void menuPrincipalCliente() {} // Menu Principal com Funcionalidades do Cliente
    public void menuPrincipalAdministrador() {} // Menu Principal com Funcionalidades do Administrador

    // Dentro do Menu do Cliente, terá as opções:
    // 1. Configuração ou Perfil
    // 2. Realizar Transação
    // 3. Visualizar Extrato
    // 4. Gestão de Cartões
    // 5. Investimentos e Poupança

    // Dentro do Menu do Administrador, terá as opções:
    // 1. Configuração ou Perfil
    // 2. Consultar/Buscar Cliente --> Bloquear Perfil, Desbloquear Perfil, Bloquear Conta,
    // Desbloquear Conta, Historico de Transacoes, Estornar Transacao
    // 3. Analisar Solicitações de Ajuste de Limite
    // 4. Manutenção de Investimentos --> Adição, Remoção ou Edição
    // 5. Relatório Geral

    public void configuracoesCliente() {} // Menu de Configurações do Cliente
    public void configuracoesAdministrador() {} // Menu de Configurações do Administrador

    // Opções:
    // 1. Excluir Perfil
    // 2. Desativar/Ativar Perfil
    // 3. Alterar Dados do Perfil
    // 4. Visualizar Dados do Perfil
    // 5. Voltar
    // Para cada opção, colete os dados necessarios e chame os metodos necessarios
    // Mostre mensagem de confirmacao da operacao no final
}
