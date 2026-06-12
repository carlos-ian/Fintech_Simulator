package Classes;

import Classes.Model.Conta.Conta;
import Classes.Model.Conta.ContaKids;
import Classes.Model.Conta.ContaPoupanca;
import Classes.Model.Operacoes.*;
import Classes.Model.Usuario.Administrador;
import Classes.Model.Usuario.Cliente;
import Classes.Model.Usuario.Usuario;
import Classes.Util.*;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.time.YearMonth;
import java.util.ArrayList;

public class AplicacaoBancaria {
    public static ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    public static ArrayList<Investimento> investimentosDisponiveis = new ArrayList<>();
    private static YearMonth ultimaAplicacaoRendimento = YearMonth.now().minusMonths(1);
    private static final int DIA_ROTINA = 12;

    public static void main(String[] args) {
        inicializarCatalogoInvestimentos();
        UsuarioBancoRepository.carregarUsuarios(listaUsuarios);
        verificarRotinas();
        AplicacaoBancaria.menuInicial();
    }

    // --------- Metodos Auxiliares --------
    private static void inicializarCatalogoInvestimentos() {
        investimentosDisponiveis.add(new Investimento("CDB Pós-Fixado", 11.5, 0, null));
        investimentosDisponiveis.add(new Investimento("LCI Isento", 9.8, 0, null));
        investimentosDisponiveis.add(new Investimento("Tesouro IPCA+", 6.2, 0, null));
        investimentosDisponiveis.add(new Investimento("Fundo de Ações", 18.5, 0, null));
        investimentosDisponiveis.add(new Investimento("Poupança", 6.17, 0, null));
    }

    private static void verificarRotinas() {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        YearMonth mesAtual = YearMonth.now();

        if (hoje.getDayOfMonth() == DIA_ROTINA && !mesAtual.equals(ultimaAplicacaoRendimento)) {
            for (Usuario usuario : listaUsuarios) {
                if (usuario instanceof Cliente) {
                    Cliente cliente = (Cliente) usuario;

                    for (Conta conta : cliente.obterContas()) {
                        if (conta instanceof ContaPoupanca) { ((ContaPoupanca) conta).aplicarRendimento(); }
                        if (conta instanceof ContaKids) { ((ContaKids) conta).resetarMes(); }

                        if (conta.getListaInvestimentos() != null && !conta.getListaInvestimentos().isEmpty()) {
                            for (Investimento inv : conta.getListaInvestimentos()) {
                                inv.aplicarRendimento();
                            }
                        }
                    }
                }
            }
            ultimaAplicacaoRendimento = mesAtual;
        }
    }



    // -------- Metodos Menus ---------------



    public static void menuInicial() {
        while (true) {
            String stringMenu = JOptionPane.showInputDialog(
                    "===============================\n" +
                            "                   SEJA BEM VINDO\n" +
                            "===============================\n" +
                            "1 - Fazer Login\n" +
                            "2 - Criar Perfil de Usuário\n" +
                            "3 - Sair\n");

            if (stringMenu == null) {
                JOptionPane.showMessageDialog(null, "--- ENCERRANDO SISTEMA ---");
                return;
            }

            int menu;
            try {
                menu = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            switch (menu) {
                case 1:
                    String loginTipo = SwingUtil.exibirMenuSelecao("Identificação de Login", "Quem é você?", "Sou Cliente", "Sou Administrador");
                    if (loginTipo != null) { Login(loginTipo); }
                    break;

                case 2:
                    String tipoCadastro = SwingUtil.exibirMenuSelecao("Criação de Perfil", "Qual perfil deseja-se criar?", "Perfil para Cliente", "Perfil para Administrador");
                    if (tipoCadastro != null) { Cadastro(tipoCadastro); }
                    break;

                case 3:
                    JOptionPane.showMessageDialog(null, "--- ENCERRANDO SISTEMA ---");
                    return;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    private static void Login(String perfilSelecionado) {
        while (true) {
            String tipoPerfil = perfilSelecionado.equals("Sou Cliente") ? "Cliente" : "Administrador";

            String[] dados = SwingUtil.exibirFormulario("Autenticação de Segurança - " + tipoPerfil, "Entre com as suas credenciais de acesso:", "CPF:", "Senha:");
            if (dados == null) { break; }

            String cpf = dados[0].trim();
            String senha = dados[1].trim();

            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            Usuario encontrado = Usuario.autenticar(cpf, senha);
            if (encontrado == null || !encontrado.getTipoUsuario().equalsIgnoreCase(tipoPerfil)) {
                JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos para o perfil " + tipoPerfil, "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (encontrado.getStatus() == Status.INATIVO) {
                String[] opcoes = {"Sim", "Não"};
                int resposta = JOptionPane.showOptionDialog(null,
                        "Seu perfil está atualmente INATIVO.\nDeseja reativar seu perfil para acessar a aplicação?",
                        "Perfil Inativo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

                if (resposta == JOptionPane.YES_OPTION) {
                    encontrado.setStatus(Status.ATIVO);
                    UsuarioBancoRepository.atualizarUsuario(encontrado.getId(), "Status", Status.ATIVO.name());
                    JOptionPane.showMessageDialog(null, "Perfil reativado com sucesso! Prossiga com o acesso.");
                } else {
                    JOptionPane.showMessageDialog(null, "Acesso negado. É necessário ativar o perfil para entrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }

            JOptionPane.showMessageDialog(null, "Autenticação " + tipoPerfil + " Concluída com Sucesso!");
            if (tipoPerfil.equals("Cliente")) {
                AplicacaoBancaria.menuGestaoContas((Cliente) encontrado);
            } else {
                AplicacaoBancaria.menuPrincipalAdministrador((Administrador) encontrado);
            }
            break;
        }
    }

    private static void Cadastro(String perfilSelecionado) {
        while (true) {
            boolean ehCliente = perfilSelecionado.equals("Perfil para Cliente");
            String[] dados;

            if (ehCliente) {
                dados = SwingUtil.exibirFormulario("Cadastro de Usuário", "Insira suas informações pessoais nos campos abaixo:",
                        "Nome:", "CPF:", "Telefone:", "Data de Nascimento (dd/mm/aaaa):", "E-mail:", "Senha:");
            } else {
                dados = SwingUtil.exibirFormulario("Cadastro de Usuário", "Insira suas informações pessoais nos campos abaixo:",
                        "Nome:", "CPF:", "Telefone:", "Data de Nascimento (dd/mm/aaaa):", "E-mail:", "Senha:", "Matrícula:");
            }

            if (dados == null) { break; }

            boolean temCampoVazio = false;
            for (String campo : dados) {
                if (campo.trim().isEmpty()) { temCampoVazio = true; break; }
            }

            if (temCampoVazio) {
                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            String nome = dados[0].trim();
            String cpf = dados[1].trim();
            String telefone = dados[2].trim();
            String dataNascimento = dados[3].trim();
            String email = dados[4].trim();
            String senha = dados[5].trim();

            boolean duplicado = false;
            for (Usuario u : AplicacaoBancaria.listaUsuarios) {
                if (u.getCpf().equals(cpf) || u.getEmail().equalsIgnoreCase(email)) {
                    duplicado = true;
                    break;
                }
            }

            if (duplicado) {
                JOptionPane.showMessageDialog(null, "CPF ou E-mail já cadastrados no sistema!", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));

            if (ehCliente) {
                Cliente cliente = new Cliente(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Cliente");
                if (UsuarioBancoRepository.salvarUsuario(cliente)) {
                    AplicacaoBancaria.listaUsuarios.add(cliente);
                    JOptionPane.showMessageDialog(null, "Perfil Criado com Sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar o perfil no banco de dados. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                String matricula = dados[6].trim();
                Administrador administrador = new Administrador(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Administrador", matricula);
                if (UsuarioBancoRepository.salvarUsuario(administrador)) {
                    AplicacaoBancaria.listaUsuarios.add(administrador);
                    JOptionPane.showMessageDialog(null, "Perfil Criado com Sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar o perfil no banco de dados. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            break;
        }
    }




    public static void menuGestaoContas(Cliente encontrado) {
        while (true) {
            if (encontrado.getStatus().equals(Status.INATIVO)) { return; }
            String stringMenu = JOptionPane.showInputDialog("=====  Menu de Contas  =====\n" +
                    "1 - Listar Contas\n" + "2 - Abrir Nova Conta\n" + "3 - Fechar Conta\n" +
                    "4 - Entrar com Conta\n" + "5 - Sair");

            if (stringMenu == null) return;

            int mpC;
            try {
                mpC = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            ContaBancoRepository.carregarContasDoCliente(encontrado);
            ArrayList<Conta> contas = encontrado.obterContas();

            switch (mpC) {
                case 1:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas cadastradas.");
                    } else {
                        StringBuilder sb = new StringBuilder("=====  SUAS CONTAS BANCÁRIAS  =====\n\n");
                        for (Conta c : contas) {
                            sb.append(c.visualizarDadosConta()).append("==================================\n\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Contas", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;

                case 2:
                    AberturaConta(encontrado);
                    break;

                case 3:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas para fechar.");
                        break;
                    }

                    String[] opcoesFechar = new String[contas.size()];
                    for (int i = 0; i < contas.size(); i++) {
                        opcoesFechar[i] = "Conta: " + contas.get(i).getNumeroConta().trim() + " [" + contas.get(i).getTipoConta() + "]";
                    }

                    String contaFecharSel = SwingUtil.exibirMenuSelecao("Encerrar Conta", "Selecione a conta que deseja fechar permanentemente:", opcoesFechar);
                    if (contaFecharSel == null) { break; }

                    try {
                        String numeroContaFechar = contaFecharSel.split("\\[")[0].replace("Conta:", "").trim();
                        Conta contaParaRemover = null;
                        for (Conta c : contas) {
                            if (c.getNumeroConta().trim().equals(numeroContaFechar)) {
                                contaParaRemover = c;
                                break;
                            }
                        }

                        if (contaParaRemover != null) {
                            boolean resultado = encontrado.fecharConta(contaParaRemover);
                            if (resultado) {
                                ContaBancoRepository.removerDoBanco(contaParaRemover.getNumeroConta());
                                JOptionPane.showMessageDialog(null, "Conta encerrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Falha ao fechar conta (Verifique se há saldo residual pendente).", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Conta não encontrada na memória do sistema.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Erro ao processar o encerramento.");
                    }
                    break;

                case 4:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas. Abra uma primeiro.");
                        break;
                    }

                    String[] opcoesEntrar = new String[contas.size()];
                    for (int i = 0; i < contas.size(); i++) {
                        opcoesEntrar[i] = "Conta: " + contas.get(i).getNumeroConta().trim() + " [" + contas.get(i).getTipoConta() + "]";
                    }

                    String contaEntrarSel = SwingUtil.exibirMenuSelecao("Acessar Conta", "Escolha em qual conta deseja entrar:", opcoesEntrar);
                    if (contaEntrarSel == null) { break; }

                    try {
                        String numeroContaEntrar = contaEntrarSel.split("\\[")[0].replace("Conta:", "").trim();
                        Conta contaSelecionada = null;
                        for (Conta c : contas) {
                            if (c.getNumeroConta().trim().equals(numeroContaEntrar)) {
                                contaSelecionada = c;
                                break;
                            }
                        }

                        if (contaSelecionada != null) {
                            menuPrincipalCliente(contaSelecionada, encontrado);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao tentar acessar a conta selecionada.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Erro ao processar os dados da conta selecionada.");
                    }
                    break;

                case 5: return;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    private static void AberturaConta(Cliente encontrado) {
        String tipo = SwingUtil.exibirMenuSelecao("Abertura de Conta", "Selecione o tipo de conta que deseja criar:", "Conta Corrente", "Conta Poupança", "Conta Kids", "Conta Investimento");
        if (tipo == null) return;

        String[] dados;
        switch (tipo) {
            case "Conta Corrente":
                dados = SwingUtil.exibirFormulario("Nova Conta Corrente", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "Limite Cheque Especial:");
                break;
            case "Conta Poupança":
                dados = SwingUtil.exibirFormulario("Nova Conta Poupança", null, "Número da Conta:", "Agência:", "Saldo Inicial:");
                break;
            case "Conta Kids":
                dados = SwingUtil.exibirFormulario("Nova Conta Kids", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "CPF do Responsável Legal:", "Limite de Movimentação Mensal:");
                break;
            default:
                dados = SwingUtil.exibirFormulario("Nova Conta Investimento", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "CPF do Titular:");
                break;
        }

        if (dados == null) return;

        try {
            String num = dados[0].trim();
            String ag = dados[1].trim();
            String saldoStr = dados[2].trim();

            if (num.isEmpty() || ag.isEmpty() || saldoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Todos os campos básicos devem ser preenchidos.");
                return;
            }

            double sal = Double.parseDouble(saldoStr.replace(",", "."));

            Conta contaCriada = null;

            if (tipo.equalsIgnoreCase("Conta Corrente")) {
                double limite = Double.parseDouble(dados[3].replace(",", "."));
                contaCriada = encontrado.abrirConta(num, ag, sal, tipo, limite, null, 0, null);
            } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                contaCriada = encontrado.abrirConta(num, ag, sal, tipo, 0, null, 0, null);
            } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                String cpfResp = dados[3].trim();
                double limMes = Double.parseDouble(dados[4].replace(",", "."));
                contaCriada = encontrado.abrirConta(num, ag, sal, tipo, 0, cpfResp, limMes, null);
            } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                String titular = dados[3].trim();
                contaCriada = encontrado.abrirConta(num, ag, sal, tipo, 0, null, 0, titular);
            }

            if (contaCriada != null) {
                ContaBancoRepository.salvarNoBanco(contaCriada, encontrado.getId());
                JOptionPane.showMessageDialog(null, "Conta bancária criada e vinculada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro local: A conta não pôde ser criada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Verifique a formatação dos campos numéricos.");
        }
    }

    public static void menuPrincipalCliente(Conta conta, Cliente encontrado) {
        int opcaoDash = 0;
        while (opcaoDash != 7) {
            if (encontrado.getStatus().equals(Status.INATIVO)) {return;}
            String stringDash = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "               DASHBOARD DA CONTA         \n" +
                            "==================================\n" +
                            "Conta: " + conta.getNumeroConta() + " | Saldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\n" +
                            "==================================\n" +
                            "1 - Configurações / Perfil\n" +
                            "2 - Visualizar Dados da Conta\n" +
                            "3 - Realizar Transação\n" +
                            "4 - Visualizar Extrato\n" +
                            "5 - Gestão de Cartões\n" +
                            "6 - Investimentos\n" +
                            "7 - Voltar para Menu de Contas\n");

            if (stringDash == null) return;

            try {
                opcaoDash = Integer.parseInt(stringDash);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            switch (opcaoDash) {
                case 1:
                    AplicacaoBancaria.configuracoes(encontrado);
                    break;

                case 2:
                    JOptionPane.showMessageDialog(null, conta.visualizarDadosConta(), "Dados da Conta", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 3:
                    ArrayList<Cartao> cartoesDaConta = conta.getCartoes();

                    Object[] dadosTx = SwingUtil.exibirFormularioTransacao(cartoesDaConta);
                    if (dadosTx == null) break;

                    try {
                        String valorStr = (String) dadosTx[0];
                        if (valorStr.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Por favor, insira um valor.", "Erro", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        double valor = Double.parseDouble(valorStr.replace(",", "."));
                        String metodo = (String) dadosTx[1];
                        int indiceCartao = (int) dadosTx[2];
                        String categoria = (String) dadosTx[3];
                        String cpfDest = (String) dadosTx[4];
                        String numContaDest = (String) dadosTx[5];

                        Cartao cartaoEscolhido = null;
                        if (indiceCartao > 0 && !cartoesDaConta.isEmpty()) {
                            cartaoEscolhido = cartoesDaConta.get(indiceCartao - 1);
                        }

                        Conta contaDestino = null;
                        if (!cpfDest.isEmpty() && !numContaDest.isEmpty()) {
                            contaDestino = ContaBancoRepository.buscarContaDestinoNoBanco(cpfDest, numContaDest);
                        }

                        if(!cpfDest.equals(encontrado.getCpf()) && conta.getTipoConta().equals("Conta Investimento")) {
                            JOptionPane.showMessageDialog(null, "Transação Inválida para Conta Investimento", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                        boolean sucesso = conta.realizarTransacao(valor, metodo, cartaoEscolhido, categoria, contaDestino);
                        if (sucesso) {
                            ArrayList<Transacao> extratoAtual = conta.getExtrato();
                            Transacao ultimaTx = extratoAtual.get(extratoAtual.size() - 1);

                            Integer idDestino = (contaDestino != null) ? contaDestino.getId() : null;
                            Double saldoDestino = (contaDestino != null) ? contaDestino.getSaldo() : null;

                            TransacaoBancoRepository.registrarTransacaoNoBanco(ultimaTx, conta.getId(), conta.getSaldo(), idDestino, saldoDestino);

                            if (contaDestino != null) {
                                Transacao txEntrada = new Transacao(ultimaTx.getData(), ultimaTx.getHora(), valor, categoria, "ENTRADA", metodo, ultimaTx.getStatus(), conta, contaDestino);
                                contaDestino.getExtrato().add(txEntrada);
                            }
                            JOptionPane.showMessageDialog(null, "Transação concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Formato de valor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro na Transação", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 4:
                    String[] filtros = SwingUtil.exibirFormulario("Filtros do Extrato", "Deixe em branco para ignorar o filtro:", "Fluxo (ENTRADA/SAÍDA):", "Método de Pagamento:", "Categoria:", "Dias de Histórico:");
                    if (filtros == null) break;

                    String fluxo = filtros[0].trim().toUpperCase();
                    String metodoExtrato = filtros[1].trim().toUpperCase();
                    String catExtrato = filtros[2].trim();
                    String diasStr = filtros[3].trim();

                    Integer dias = null;
                    if (!diasStr.isEmpty()) {
                        try {
                            dias = Integer.parseInt(diasStr);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Filtro de dias inválido e ignorado.");
                        }
                    }

                    ArrayList<Transacao> extratoFiltrado = conta.visualizarExtrato(fluxo.isEmpty() ? null : fluxo, metodoExtrato.isEmpty() ? null : metodoExtrato, catExtrato.isEmpty() ? null : catExtrato, dias, null);

                    if (extratoFiltrado == null || extratoFiltrado.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma transação encontrada.", "Extrato Vazio", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sb = new StringBuilder("=== EXTRATO DE TRANSAÇÕES ===\n\n");
                        for (Transacao t : extratoFiltrado) {
                            sb.append(String.format("[%s às %s] - %s\n", t.getData(), t.getHora(), t.getCategoria()));
                            sb.append(String.format("Método: %s | Tipo: %s\n", t.getMetodoPagamento(), t.getFluxo()));
                            sb.append(String.format("Valor: R$ %.2f\n", t.getValor()));
                            sb.append("--------------------------------------------------\n");
                        }

                        JTextArea textArea = new JTextArea(15, 40);
                        textArea.setText(sb.toString());
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Extrato Consolidado", JOptionPane.PLAIN_MESSAGE);
                    }
                    break;

                case 5:
                    AplicacaoBancaria.gestaoCartoes(conta, encontrado);
                    break;

                case 6:
                    AplicacaoBancaria.investimentosEPoupanca(conta, encontrado);
                    break;

                case 7:
                    JOptionPane.showMessageDialog(null, "Voltando ao menu de contas...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    public static void configuracoes(Usuario encontrado) {
        int config = 0;
        while (config != 5) {
            String stringConfig = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "       CONFIGURAÇÕES DE PERFIL    \n" +
                            "==================================\n" +
                            "1 - Excluir Perfil\n" +
                            "2 - Alterar Dados do Perfil\n" +
                            "3 - Visualizar Dados do Perfil\n" +
                            "4 - Ativar / Desativar Perfil \n" +
                            "5 - Voltar\n");

            if (stringConfig == null) return;

            try {
                config = Integer.parseInt(stringConfig);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            switch (config) {
                case 1:
                    String[] confirmacaoExclusao = SwingUtil.exibirFormulario("Confirmação de Segurança", "ESTA AÇÃO É IRREVERSÍVEL!", "Confirme sua Senha:");
                    if (confirmacaoExclusao == null || confirmacaoExclusao[0].isEmpty()) break;

                    try {
                        encontrado.encerrarPerfil(encontrado, confirmacaoExclusao[0]);
                        UsuarioBancoRepository.deletarUsuario(encontrado.getId());
                        JOptionPane.showMessageDialog(null, "Perfil excluído com sucesso. Desconectando do sistema...", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    String tipoDado = SwingUtil.exibirMenuSelecao("Alteração Cadastral", "Selecione a informação que deseja modificar:", "Nome", "CPF", "Email", "Senha", "Telefone", "DataNascimento");
                    if (tipoDado == null) break;

                    String[] novoValForm = SwingUtil.exibirFormulario("Atualização cadastral", null, "Digite o novo valor para " + tipoDado + ":");
                    if (novoValForm == null) break;

                    String novoValor = novoValForm[0].trim();
                    if (novoValor.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo não pode ser vazio.", "Erro", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    try {
                        encontrado.alterarDados(tipoDado, novoValor);

                        String valorParaOBanco = novoValor;
                        if ("Senha".equalsIgnoreCase(tipoDado)) {valorParaOBanco = encontrado.getSenha();}
                        UsuarioBancoRepository.atualizarUsuario(encontrado.getId(), tipoDado, valorParaOBanco);

                        JOptionPane.showMessageDialog(null, tipoDado + " atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3:
                    JOptionPane.showMessageDialog(null, encontrado.visualizarDados(), "Dados do Perfil", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 4:
                    String statusEscolhido = SwingUtil.exibirMenuSelecao("Desativar/Ativar Perfil", "Escolha o Novo Status do Perfil:", "ATIVO", "INATIVO");
                    if (statusEscolhido == null) break;

                    try {
                        Status novoStatus = Status.valueOf(statusEscolhido);

                        if (encontrado.getStatus() == novoStatus) {
                            JOptionPane.showMessageDialog(null, "O perfil já possui este status.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                        encontrado.setStatus(novoStatus);
                        UsuarioBancoRepository.atualizarUsuario(encontrado.getId(), "Status", Status.ATIVO.name());
                        JOptionPane.showMessageDialog(null, "Status alterado para " + statusEscolhido + " com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        if (novoStatus == Status.INATIVO) {
                            JOptionPane.showMessageDialog(null, "Como seu perfil foi inativado, você será desconectado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao processar alteração de status.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, "Voltando ao Dashboard...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    private static void gestaoCartoes(Conta conta, Cliente encontrado) {
        int opcaoCartao = 0;
        while (opcaoCartao != 7) {
            String stringCartao = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "               GESTÃO DE CARTÕES        \n" +
                            "==================================\n" +
                            "1 - Criar Novo Cartão\n" +
                            "2 - Visualizar Dados do Cartão\n" +
                            "3 - Bloquear Cartão\n" +
                            "4 - Desbloquear Cartão\n" +
                            "5 - Visualizar Limite\n" +
                            "6 - Solicitar Ajuste de Limite\n" +
                            "7 - Voltar\n");

            if (stringCartao == null) return;

            try {
                opcaoCartao = Integer.parseInt(stringCartao);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            Cartao cartaoSelecionado = null;
            if (opcaoCartao >= 2 && opcaoCartao <= 6) {
                ArrayList<Cartao> listaCartoes = conta.getCartoes();
                if (listaCartoes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Esta conta não possui cartões emitidos.");
                    continue;
                }

                String[] opcoesCartao = new String[listaCartoes.size()];
                for (int i = 0; i < listaCartoes.size(); i++) {
                    String num = listaCartoes.get(i).getNumeroCartao();
                    opcoesCartao[i] = "ID: " + i + " | Cartão Final: " + (num.length() >= 4 ? num.substring(num.length() - 4) : num);
                }

                String escolhaCartao = SwingUtil.exibirMenuSelecao("Seleção do Cartão", "Escolha o cartão para realizar a operação:", opcoesCartao);
                if (escolhaCartao == null) continue;

                int idCartao = Integer.parseInt(escolhaCartao.split(" ")[1]);
                cartaoSelecionado = listaCartoes.get(idCartao);
            }

            switch (opcaoCartao) {
                case 1:
                    String[] dadosNovoCartao = SwingUtil.exibirFormulario("Criação de Novo Cartão", "Insira as seguintes informações:", "Número do Cartão (16 dígitos):", "Tipo (DEBITO/CREDITO):", "Limite de Crédito:");
                    if (dadosNovoCartao == null) break;

                    String numero = dadosNovoCartao[0].trim();
                    String tipo = dadosNovoCartao[1].trim().toUpperCase();
                    String limiteStr = dadosNovoCartao[2].trim();

                    if (numero.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O número do cartão é obrigatório.", "Erro", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    try {
                        double limiteInput = Double.parseDouble(limiteStr.replace(",", "."));
                        double limiteInicial = tipo.equalsIgnoreCase("DEBITO") ? 0.0 : limiteInput;

                        Cartao novoCartao = new Cartao(numero, encontrado.getNome(), tipo, limiteInicial, limiteInicial);
                        conta.getCartoes().add(novoCartao);

                        CartaoBancoRepository.salvarNoBanco(novoCartao, conta.getId());

                        JOptionPane.showMessageDialog(null, "Cartão gerado e vinculado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Insira um valor numérico válido para o limite.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    JOptionPane.showMessageDialog(null, cartaoSelecionado.visualizarDados(), "Informações do Cartão", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 3:
                    if (cartaoSelecionado.bloquearCartao()) {
                        CartaoBancoRepository.atualizarBloqueioNoBanco(cartaoSelecionado.getNumeroCartao(), true);
                        JOptionPane.showMessageDialog(null, "O cartão foi bloqueado com sucesso.", "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Este cartão já se encontra bloqueado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 4:
                    if (cartaoSelecionado.ativarCartao()) {
                        CartaoBancoRepository.atualizarBloqueioNoBanco(cartaoSelecionado.getNumeroCartao(), false);
                        JOptionPane.showMessageDialog(null, "O cartão foi ativado/desbloqueado com sucesso.", "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Este cartão já está ativo no sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, cartaoSelecionado.visualizarLimites(), "Limites Comerciais", JOptionPane.INFORMATION_MESSAGE);
                    break;


                case 6:
                    String[] novoLimiteForm = SwingUtil.exibirFormulario("Ajuste de Limite", "Solicitação de alteração de linha de crédito:", "Informe o novo limite total desejado:");
                    if (novoLimiteForm == null || novoLimiteForm[0].isEmpty()) break;

                    try {
                        double novoLimite = Double.parseDouble(novoLimiteForm[0].replace(",", "."));

                        Administrador admSistema = null;
                        for (Usuario u : AplicacaoBancaria.listaUsuarios) {
                            if (u instanceof Administrador) {
                                admSistema = (Administrador) u;
                                break;
                            }
                        }

                        if (cartaoSelecionado.solicitarAjusteLimite(novoLimite, admSistema, conta)) {
                            CartaoBancoRepository.atualizarLimitesNoBanco(cartaoSelecionado.getNumeroCartao(), cartaoSelecionado.getLimiteTotal(), cartaoSelecionado.getLimiteDisponivel());
                            JOptionPane.showMessageDialog(null, "Ajuste aprovado pelo Administrador!\nNovo limite estabelecido com sucesso.", "Análise Concluída", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ajuste recusado após análise de perfil de crédito ou valor inválido.", "Análise Concluída", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Valor informado de forma incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Operação Inválida", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 7:
                    JOptionPane.showMessageDialog(null, "Voltando ao Dashboard...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    public static void investimentosEPoupanca(Conta conta, Cliente encontrado) {
        int opcaoInv = 0;

        while (opcaoInv != 5) {
            String stringInv = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "     INVESTIMENTOS E POUPANÇA     \n" +
                            "==================================\n" +
                            "Saldo Livre Atual: R$ " + String.format("%.2f", conta.getSaldo()) + "\n" +
                            "==================================\n" +
                            "1 - Visualizar Investimentos Disponíveis\n" +
                            "2 - Visualizar Meus Investimentos\n" +
                            "3 - Realizar Aplicação\n" +
                            "4 - Resgatar Aplicação\n" +
                            "5 - Voltar\n");

            if (stringInv == null) return;

            try {
                opcaoInv = Integer.parseInt(stringInv);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            Investimento helper = new Investimento(null, 0, 0, null);
            ArrayList<Investimento> disponiveis = AplicacaoBancaria.investimentosDisponiveis;

            switch (opcaoInv) {
                case 1:
                    if (disponiveis == null || disponiveis.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há produtos de investimento cadastrados no momento.", "Investimentos", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sbDisponiveis = new StringBuilder("=== TÍTULOS DISPONÍVEIS PARA APLICAÇÃO ===\n\n");
                        for (int i = 0; i < disponiveis.size(); i++) {
                            Investimento inv = disponiveis.get(i);
                            sbDisponiveis.append("ID: ").append(i).append("\n");
                            sbDisponiveis.append("Produto: ").append(inv.getNomeProduto()).append("\n");
                            sbDisponiveis.append("Taxa de Rendimento: ").append(inv.getTaxaRendimento()).append("% a.a.\n");
                            sbDisponiveis.append("-------------------------------------------\n");
                        }

                        JTextArea txtAreaDisponiveis = new JTextArea(15, 40);
                        txtAreaDisponiveis.setText(sbDisponiveis.toString());
                        txtAreaDisponiveis.setEditable(false);

                        JOptionPane.showMessageDialog(null, new JScrollPane(txtAreaDisponiveis), "Mercado de Ativos", JOptionPane.PLAIN_MESSAGE);
                    }
                    break;

                case 2:
                    ArrayList<Investimento> feitos = conta.getListaInvestimentos();

                    if (feitos == null || feitos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há aplicações ativas nesta conta.", "Investimentos", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sbFeitos = new StringBuilder("=== INVESTIMENTOS FEITOS ===\n\n");
                        for (int i = 0; i < feitos.size(); i++) {
                            Investimento inv = feitos.get(i);
                            sbFeitos.append("ID: ").append(i).append("\n");
                            sbFeitos.append(inv.toString()).append("\n-----------------------\n");
                        }

                        JTextArea txtArea = new JTextArea(15, 40);
                        txtArea.setText(sbFeitos.toString());
                        txtArea.setEditable(false);
                        JOptionPane.showMessageDialog(null, new JScrollPane(txtArea), "Aplicações Consolidadas", JOptionPane.PLAIN_MESSAGE);
                    }
                    break;

                case 3:
                    if (disponiveis == null || disponiveis.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há investimentos disponíveis para aplicação.", "Erro", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    String[] opcoesCat = new String[disponiveis.size()];
                    for (int i = 0; i < disponiveis.size(); i++) {
                        opcoesCat[i] = "ID: " + i + " | " + disponiveis.get(i).getNomeProduto() + " (" + disponiveis.get(i).getTaxaRendimento() + "% a.a.)";
                    }

                    String produtoEscolhido = SwingUtil.exibirMenuSelecao("Investimentos Disponíveis", "Selecione em qual título deseja aplicar o capital:", opcoesCat);
                    if (produtoEscolhido == null) break;

                    String[] valorForm = SwingUtil.exibirFormulario("Realização de Investimento", null, "Valor para Investir (R$):");
                    if (valorForm == null || valorForm[0].isEmpty()) break;

                    try {
                        int indexSelecionado = Integer.parseInt(produtoEscolhido.split(" ")[1]);
                        double valorInvestir = Double.parseDouble(valorForm[0].replace(",", "."));

                        Investimento produtoSelecionado = disponiveis.get(indexSelecionado);
                        String dataAtual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        if (helper.realizarInvestimento(conta, produtoSelecionado, valorInvestir, dataAtual)) {
                            InvestimentoBancoRepository.registrarAplicacao(conta, produtoSelecionado, valorInvestir, dataAtual);
                            JOptionPane.showMessageDialog(null, "Aplicação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Valor numérico digitado incorretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na Operação", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 4:
                    if (conta.getListaInvestimentos() == null || conta.getListaInvestimentos().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui nenhuma aplicação para resgatar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    String[] ativosParaResgate = new String[conta.getListaInvestimentos().size()];
                    for (int i = 0; i < conta.getListaInvestimentos().size(); i++) {
                        ativosParaResgate[i] = "ID: " + i + " | Ativo: " + conta.getListaInvestimentos().get(i).getNomeProduto();
                    }

                    String resgateSel = SwingUtil.exibirMenuSelecao("Resgate de Investimento", "Escolha qual investimento deseja resgatar:", ativosParaResgate);
                    if (resgateSel == null) break;

                    try {
                        int posicaoSelecionada = Integer.parseInt(resgateSel.split(" ")[1]);

                        Investimento invParaResgatar = conta.getListaInvestimentos().get(posicaoSelecionada);
                        String nomeAtivo = invParaResgatar.getNomeProduto();
                        double valorAtivo = invParaResgatar.getValorAplicado();
                        int idInternoDoObjeto = invParaResgatar.getId();

                        if (helper.resgatarInvestimento(conta, idInternoDoObjeto)) {
                            InvestimentoBancoRepository.registrarResgate(conta, nomeAtivo, valorAtivo);
                            JOptionPane.showMessageDialog(null, "Resgate concluído! O valor retornou ao seu saldo livre.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Falha no Resgate", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, "Voltando ao Dashboard...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    public static void menuPrincipalAdministrador(Administrador admin) {
        int mpA = 0;

        while (mpA != 5) {
            if (admin.getStatus().equals(Status.INATIVO)) {return;}
            String stringMenu = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "       PAINEL ADMINISTRATIVO      \n" +
                            "==================================\n" +
                            "Olá, " + admin.getNome() + " | Matrícula: " + admin.getMatricula() + "\n" +
                            "==================================\n" +
                            "1 - Configurações / Meu Perfil\n" +
                            "2 - Gerenciar Clientes (Buscar, Ativar, Desativar)\n" +
                            "3 - Gerenciar Contas (Bloquear, Desbloquear, Histórico)\n" +
                            "4 - Gerar Relatório Geral Fintech\n" +
                            "5 - Voltar para a Tela de Login\n");

            if (stringMenu == null) return;

            try {
                mpA = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            switch (mpA) {
                case 1:
                    AplicacaoBancaria.configuracoes((Usuario) admin);
                    break;

                case 2:
                    String cpfBusca = JOptionPane.showInputDialog("Digite o CPF do Cliente que deseja gerenciar:");
                    if (cpfBusca == null || cpfBusca.trim().isEmpty()) break;

                    Cliente clienteEncontrado = admin.consultarCliente(cpfBusca.trim(), null);

                    if (clienteEncontrado == null) {
                        JOptionPane.showMessageDialog(null, "Cliente não localizado na base de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    int acaoCliente = SwingUtil.exibirMenuBotoes("Gerenciar Clientes",
                            "Cliente: " + clienteEncontrado.getNome() + "\nStatus Atual: " + clienteEncontrado.getStatus(),
                            "Ativar Perfil", "Desativar Perfil", "Cancelar");

                    if (acaoCliente == 0) {
                        boolean mudou = admin.ativarPerfilCliente(clienteEncontrado);
                        UsuarioBancoRepository.atualizarUsuario(clienteEncontrado.getId(), "Status", clienteEncontrado.getStatus().name());
                        JOptionPane.showMessageDialog(null, mudou ? "Perfil ativado com sucesso!" : "O perfil já estava ativo.");
                    } else if (acaoCliente == 1) {
                            Justificativa motivoSel = (Justificativa) JOptionPane.showInputDialog(
                                    null,
                                    "Selecione o motivo da desativação do perfil:",
                                    "Desativação",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    Justificativa.values(),
                                    Justificativa.values()[0]
                            );

                            if (motivoSel != null) {
                                boolean mudou = admin.desativarPerfilCliente(clienteEncontrado, motivoSel);
                                UsuarioBancoRepository.atualizarUsuario(clienteEncontrado.getId(), "Status", clienteEncontrado.getStatus().name());
                                JOptionPane.showMessageDialog(null, mudou ? "Perfil desativado com sucesso!" : "O perfil já estava inativo.");
                            }
                        }
                    break;

                case 3:
                    String cpfContaBusca = JOptionPane.showInputDialog("Digite o CPF do titular da conta:");
                    if (cpfContaBusca == null || cpfContaBusca.trim().isEmpty()) break;

                    Cliente titular = admin.consultarCliente(cpfContaBusca.trim(), null);
                    if (titular == null || titular.obterContas().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Titular não possui contas registradas na base.");
                        break;
                    }

                    String[] contasStr = new String[titular.obterContas().size()];
                    for (int i = 0; i < titular.obterContas().size(); i++) {
                        contasStr[i] = "Nº Conta: " + titular.obterContas().get(i).getNumeroConta() + " [" + titular.obterContas().get(i).getTipoConta() + "]";
                    }

                    String contaSel = SwingUtil.exibirMenuSelecao("Seleção de Conta", "Selecione qual conta auditar:", contasStr);
                    if (contaSel == null) break;

                    Conta contaAlvo = null;
                    for (Conta c : titular.obterContas()) {
                        if (contaSel.contains(c.getNumeroConta())) {
                            contaAlvo = c;
                            break;
                        }
                    }

                    int acaoConta = SwingUtil.exibirMenuBotoes("Painel de Auditoria - Conta: " + contaAlvo.getNumeroConta(),
                            "Selecione o procedimento de controle de risco corporativo:", "Bloquear", "Desbloquear", "Ver Histórico/Logs", "Cancelar");

                    if (acaoConta == 0) {
                        Justificativa motivoSel = (Justificativa) JOptionPane.showInputDialog(
                                null,
                                "Selecione o motivo do bloqueio da conta:",
                                "Controle de Risco Corporativo",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                Justificativa.values(),
                                Justificativa.values()[0]
                        );

                        if (motivoSel != null) {
                            boolean ok = admin.desativarConta(contaAlvo, motivoSel);
                            ContaBancoRepository.atualizarStatusContaNoBanco(contaAlvo.getId(), contaAlvo.getStatus());
                            JOptionPane.showMessageDialog(null, ok ? "Conta bloqueada com sucesso." : "A conta já está bloqueada.");
                        }
                    } else if (acaoConta == 1) {
                        boolean ok = admin.ativarConta(contaAlvo);
                        ContaBancoRepository.atualizarStatusContaNoBanco(contaAlvo.getId(), contaAlvo.getStatus());
                        JOptionPane.showMessageDialog(null, ok ? "Conta desbloqueada com sucesso." : "A conta não estava bloqueada.");
                    } else if (acaoConta == 2) {
                        ArrayList<Transacao> historico = admin.visualizarHistoricoConta(contaAlvo);
                        if (historico == null || historico.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nenhuma transação registrada nesta conta.");
                        } else {
                            StringBuilder sb = new StringBuilder("=== AUDITORIA DE HISTÓRICO ===\n\n");
                            for (Transacao t : historico) {
                                sb.append(String.format("[%s às %s] R$ %.2f (%s) - Tipo: %s | Status: %s\n",
                                        t.getData(), t.getHora(), t.getValor(), t.getMetodoPagamento(), t.getFluxo(), t.getStatus()));
                                sb.append("--------------------------------------------------\n");
                            }
                            JTextArea ta = new JTextArea(15, 45);
                            ta.setText(sb.toString());
                            ta.setEditable(false);
                            JOptionPane.showMessageDialog(null, new JScrollPane(ta), "Auditoria de Conta", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                    break;

                case 4:
                    ArrayList<Cliente> todosClientes = new ArrayList<>();
                    ArrayList<Conta> todasContas = new ArrayList<>();
                    ArrayList<Transacao> todasTransacoes = new ArrayList<>();

                    for (Usuario u : AplicacaoBancaria.listaUsuarios) {
                        if (u instanceof Cliente) {
                            Cliente c = (Cliente) u;
                            todosClientes.add(c);
                            for (Conta con : c.obterContas()) {
                                todasContas.add(con);
                                if (con.getExtrato() != null) {
                                    todasTransacoes.addAll(con.getExtrato());
                                }
                            }
                        }
                    }

                    String relatorio = admin.gerarRelatorioFintech(todosClientes, todasContas, todasTransacoes);

                    JTextArea txtRelatorio = new JTextArea(18, 50);
                    txtRelatorio.setText(relatorio);
                    txtRelatorio.setEditable(false);
                    txtRelatorio.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

                    JOptionPane.showMessageDialog(null, new JScrollPane(txtRelatorio), "Sistema Analítica da Fintech", JOptionPane.PLAIN_MESSAGE);

                    JOptionPane.showMessageDialog(null, "Cópia do relatório exportada para 'relatorio_fintech.txt' com sucesso!", "Exportação", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, "Voltando para Menu Inicial...");
                    return;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }
}