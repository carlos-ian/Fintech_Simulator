package Classes;

import javax.swing.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class AplicacaoBancaria {
    public static ArrayList<Usuario> ListaUsuarios = new ArrayList<>();
    public static ArrayList<Investimento> produtosDisponiveis = new ArrayList<>();

    public static void main(String[] args) {
        AplicacaoBancaria.produtosDisponiveis.add(new Investimento("CDB Pós-Fixado", 11.5, 6, "Renda Fixa", "Garantido pelo FGC"));
        AplicacaoBancaria.produtosDisponiveis.add(new Investimento("LCI Isento", 9.8, 12, "Renda Fixa", "Isento de Imposto de Renda"));
        AplicacaoBancaria.produtosDisponiveis.add(new Investimento("Tesouro IPCA+", 6.2, 24, "Título Público", "Proteção contra a inflação"));
        AplicacaoBancaria.produtosDisponiveis.add(new Investimento("Fundo de Ações", 18.5, 0, "Renda Variável", "Maior risco e maior potencial"));
        AplicacaoBancaria.produtosDisponiveis.add(new Investimento("Poupança", 6.17, 0, "Renda Fixa", "Liquidez diária garantida"));

        int menu = 0;
        while (menu == 0) {
            String stringMenu = JOptionPane.showInputDialog(
                    "===============================\n" +
                            "SEJA BEM VINDO AO NOSSO SISTEMA\n" +
                            "===============================\n" +
                            "1 - Fazer Login\n" +
                            "2 - Criar uma conta\n" +
                            "3 - Sair\n");

            if (stringMenu == null) {System.out.println("Sistema encerrado pelo usuário."); return;}

            try {
                menu = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                menu = 0;
                continue;
            }

            switch (menu) {
                case 1:
                    Login();
                    menu = 0;
                    break;
                case 2:
                    Cadastro();
                    menu = 0;
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "...ENCERRANDO O SISTEMA...");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    menu = 0;
                    break;
            }
        }
    }
    public static void menuGestaoCartoes(Cliente encontrado) {
        int mpC = 0;

        while (mpC == 0) {
            String stringMenu = JOptionPane.showInputDialog(
                    "1 - Listar Contas\n" +
                            "2 - Abrir Nova Conta\n" +
                            "3 - Fechar Conta\n" +
                            "4 - Entrar com Conta\n" +
                            "5 - Sair"
            );

            if (stringMenu == null) return;

            try {
                mpC = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                mpC = 0;
                continue;
            }

            ArrayList<Conta> contas = encontrado.obterContas();

            switch (mpC) {
                case 1:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas cadastradas.");
                    } else {
                        for (Conta c : contas) {
                            JOptionPane.showMessageDialog(null, c.visualizarDadosConta());
                        }
                    }
                    mpC = 0;
                    break;

                case 2:
                    AberturaConta(encontrado);
                    mpC = 0;
                    break;

                case 3:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas para fechar.");
                        mpC = 0;
                        break;
                    }

                    for (int i = 0; i < contas.size(); i++) {
                        JOptionPane.showMessageDialog(null, "ID: " + i + "\n" + contas.get(i).visualizarDadosConta());
                    }

                    try {
                        String idStr = JOptionPane.showInputDialog("Qual o id da conta que deseja-se excluir?");
                        if (idStr == null) { mpC = 0; break; }

                        int id = Integer.parseInt(idStr);
                        boolean resultado = encontrado.fecharConta(contas.get(id));

                        if (resultado) {
                            JOptionPane.showMessageDialog(null, "Sucesso");
                        } else {
                            JOptionPane.showMessageDialog(null, "Falha ao remover");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.");
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(null, "ID não encontrado na lista.");
                    }
                    mpC = 0;
                    break;

                case 4:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas. Abra uma primeiro.");
                        mpC = 0;
                        break;
                    }

                    for (int i = 0; i < contas.size(); i++) {
                        JOptionPane.showMessageDialog(null, "ID: " + i + "\n" + contas.get(i).visualizarDadosConta());
                    }

                    try {
                        String idStr = JOptionPane.showInputDialog("Qual o id da conta que deseja-se entrar?");
                        if (idStr == null) { mpC = 0; break; }

                        int id = Integer.parseInt(idStr);
                        Conta contaSelecionada = contas.get(id);

                        menuPrincipalCliente(contaSelecionada, encontrado);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.");
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(null, "ID não encontrado.");
                    }
                    mpC = 0;
                    break;

                case 5:
                    return;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    mpC = 0;
                    break;
            }
        }
    }
    public static void menuPrincipalCliente(Conta conta, Cliente encontrado) {
        int opcaoDash = 0;

        while (opcaoDash != 7) {
            String stringDash = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "       DASHBOARD DA CONTA         \n" +
                            "==================================\n" +
                            "Conta: " + conta.getNumeroConta() + " | Saldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\n" +
                            "==================================\n" +
                            "1 - Configurações / Perfil\n" +
                            "2 - Visualizar Dados da Conta\n" +
                            "3 - Realizar Transação\n" +
                            "4 - Visualizar Extrato\n" +
                            "5 - Gestão de Cartões\n" +
                            "6 - Investimentos e Poupança\n" +
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
                    AplicacaoBancaria.configuracoes((Usuario) encontrado);
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
                        if (indiceCartao > 0 && cartoesDaConta != null && !cartoesDaConta.isEmpty()) {
                            cartaoEscolhido = cartoesDaConta.get(indiceCartao - 1);
                        }

                        Conta contaDestino = null;
                        if (!cpfDest.isEmpty() && !numContaDest.isEmpty()) {
                            for (Usuario u : AplicacaoBancaria.ListaUsuarios) {
                                if (u instanceof Cliente && u.getCpf().equals(cpfDest)) {
                                    for (Conta c : ((Cliente) u).obterContas()) {
                                        if (c.getNumeroConta().equals(numContaDest)) {
                                            contaDestino = c;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (contaDestino == null) {
                                JOptionPane.showMessageDialog(null, "Conta de destino não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                        }

                        boolean sucesso = conta.realizarTransacao(valor, metodo, cartaoEscolhido, categoria, contaDestino);
                        if (sucesso) {
                            JOptionPane.showMessageDialog(null, "Transação concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Formato de valor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro na Transação", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 4:
                    String[] filtros = SwingUtil.exibirFiltrosExtrato();
                    if (filtros == null) break;

                    String fluxo = filtros[0];
                    String metodoExtrato = filtros[1];
                    String catExtrato = filtros[2];
                    String diasStr = filtros[3];

                    Integer dias = null;
                    if (!diasStr.trim().isEmpty()) {
                        try {
                            dias = Integer.parseInt(diasStr.trim());
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Filtro de dias inválido e ignorado.");
                        }
                    }

                    ArrayList<Transacao> extratoFiltrado = conta.visualizarExtrato(fluxo, metodoExtrato, catExtrato, dias, null);

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
                    String senhaDigitada = SwingUtil.exibirConfirmacaoExclusao();
                    if (senhaDigitada == null) break;

                    try {
                        encontrado.encerrarPerfil(encontrado, senhaDigitada);
                        JOptionPane.showMessageDialog(null, "Perfil excluído com sucesso. Desconectando do sistema...", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    String[] dadosAlteracao = SwingUtil.exibirFormularioAlterarDados();
                    if (dadosAlteracao == null) break;

                    String tipoDado = dadosAlteracao[0];
                    String novoValor = dadosAlteracao[1];

                    if (novoValor.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo não pode ser vazio.", "Erro", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    try {
                        encontrado.alterarDados(tipoDado, novoValor);
                        JOptionPane.showMessageDialog(null, tipoDado + " atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3: // VISUALIZAR DADOS
                    JOptionPane.showMessageDialog(null, encontrado.visualizarDados(), "Dados do Perfil", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 4:
                    String statusAtual = (encontrado.getStatus() != null) ? encontrado.getStatus().toString() : "DESCONHECIDO";
                    String statusEscolhido = SwingUtil.exibirSeletorStatus(statusAtual);
                    if (statusEscolhido == null) break;

                    try {
                        Status novoStatus = Status.valueOf(statusEscolhido);

                        if (encontrado.getStatus() == novoStatus) {
                            JOptionPane.showMessageDialog(null, "O perfil já possui este status.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                        encontrado.setStatus(novoStatus);
                        JOptionPane.showMessageDialog(null, "Status alterado para " + statusEscolhido + " com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        if (novoStatus == Status.INATIVO) {
                            JOptionPane.showMessageDialog(null, "Como seu perfil foi inativado, você será desconectado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            System.exit(0);
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
                            "         GESTÃO DE CARTÕES        \n" +
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
                cartaoSelecionado = SwingUtil.exibirSeletorCartoes(conta.getCartoes());
                if (cartaoSelecionado == null) continue;
            }

            switch (opcaoCartao) {
                case 1:
                    String[] dadosNovoCartao = SwingUtil.exibirFormularioCriarCartao();
                    if (dadosNovoCartao == null) break;

                    String numero = dadosNovoCartao[0];
                    String tipo = dadosNovoCartao[1];
                    String limiteStr = dadosNovoCartao[2];

                    if (numero.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O número do cartão é obrigatório.", "Erro", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    try {
                        double limiteInput = Double.parseDouble(limiteStr.replace(",", "."));
                        double limiteInicial = tipo.equalsIgnoreCase("DEBITO") ? 0.0 : limiteInput;

                        Cartao novoCartao = new Cartao(numero, encontrado.getNome(), tipo, limiteInicial, limiteInicial);
                        conta.getCartoes().add(novoCartao);

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
                        JOptionPane.showMessageDialog(null, "O cartão foi bloqueado com sucesso.", "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Este cartão já se encontra bloqueado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 4:
                    if (cartaoSelecionado.ativarCartao()) {
                        JOptionPane.showMessageDialog(null, "O cartão foi ativado/desbloqueado com sucesso.", "Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Este cartão já está ativo no sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, cartaoSelecionado.visualizarLimites(), "Limites Comerciais", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 6:
                    String novoLimiteStr = SwingUtil.exibirFormularioAjusteLimite();
                    if (novoLimiteStr == null) break;

                    try {
                        double novoLimite = Double.parseDouble(novoLimiteStr.replace(",", "."));

                        Administrador admSistema = null;
                        for (Usuario u : AplicacaoBancaria.ListaUsuarios) {
                            if (u instanceof Administrador) {
                                admSistema = (Administrador) u;
                                break;
                            }
                        }

                        if (admSistema == null) {
                            JOptionPane.showMessageDialog(null, "Operação indisponível: Nenhum Administrador ativo no sistema para homologar a análise.", "Erro do Sistema", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        if (cartaoSelecionado.solicitarAjusteLimite(novoLimite, admSistema, conta)) {
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

        while (opcaoInv != 4) {
            String stringInv = JOptionPane.showInputDialog(
                    "==================================\n" +
                            "     INVESTIMENTOS E POUPANÇA     \n" +
                            "==================================\n" +
                            "Saldo Livre Atual: R$ " + String.format("%.2f", conta.getSaldo()) + "\n" +
                            "==================================\n" +
                            "1 - Visualizar Meus Investimentos\n" +
                            "2 - Realizar Aplicação\n" +
                            "3 - Resgatar Aplicação\n" +
                            "4 - Voltar\n");

            if (stringInv == null) return;

            try {
                opcaoInv = Integer.parseInt(stringInv);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                continue;
            }

            Investimento helper = new Investimento(null, 0, 0, null, null);

            switch (opcaoInv) {
                case 1:
                    ArrayList<Investimento> feitos = conta.listaInvestimentos;

                    if (feitos == null || feitos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há aplicações ativas nesta conta.", "Investimentos", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sbFeitos = new StringBuilder("=== SEU PORTFÓLIO DE ATIVOS ===\n\n");
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

                case 2:
                    Object[] dadosNovoInv = SwingUtil.exibirFormularioNovoInvestimento(AplicacaoBancaria.produtosDisponiveis);
                    if (dadosNovoInv == null) break;

                    try {
                        int indexSelecionado = (int) dadosNovoInv[0];
                        String valorStr = (String) dadosNovoInv[1];
                        double valorInvestir = Double.parseDouble(valorStr.replace(",", "."));

                        Investimento produtoSelecionado = AplicacaoBancaria.produtosDisponiveis.get(indexSelecionado);
                        String dataAtual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        if (helper.realizarInvestimento(conta, produtoSelecionado, valorInvestir, dataAtual)) {
                            JOptionPane.showMessageDialog(null, "Aplicação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: Valor numérico digitado incorretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na Operação", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3:
                    if (conta.listaInvestimentos == null || conta.listaInvestimentos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui nenhuma aplicação para resgatar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    String idResgateStr = SwingUtil.exibirFormularioResgateInvestimento();
                    if (idResgateStr == null) break;

                    try {
                        int idInvestimento = Integer.parseInt(idResgateStr);

                        if (helper.resgatarInvestimento(conta, idInvestimento)) {
                            JOptionPane.showMessageDialog(null, "Resgate concluído! O valor retornou ao seu saldo livre.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Erro: O ID deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Falha no Resgate", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case 4:
                    JOptionPane.showMessageDialog(null, "Voltando ao Dashboard...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    private static void Login() {
        String stringLogin = JOptionPane.showInputDialog("Quem é você?\n" +
                "1 - Sou Cliente\n" +
                "2 - Sou Administrador\n" +
                "3 - Voltar");

        if (stringLogin == null) return;

        int login;
        try {
            login = Integer.parseInt(stringLogin);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
            return;
        }

        if (login == 1) {
            while (true) {
                String[] dados = SwingUtil.exibirFormularioLogin();
                if (dados == null) { System.out.println("Login cancelado."); break; }

                String cpf = dados[0];
                String senha = dados[1];

                if (cpf.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    Usuario encontrado = Usuario.autenticar(cpf, senha);
                    if (encontrado == null) {
                        JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos");
                    } else if (encontrado.tipoUsuario.equalsIgnoreCase("Cliente")) {
                        JOptionPane.showMessageDialog(null, "Sucesso");
                        AplicacaoBancaria.menuGestaoCartoes((Cliente) encontrado);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciais inválidas para este tipo de Usuário");
                    }
                }
            }
        } else if (login == 2) {
            while (true) {
                String[] dados = SwingUtil.exibirFormularioLogin();
                if (dados == null) { System.out.println("Login cancelado."); break; }

                String cpf = dados[0];
                String senha = dados[1];

                if (cpf.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    Usuario encontrado = Usuario.autenticar(cpf, senha);
                    if (encontrado == null) {
                        JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos");
                    } else if (encontrado.tipoUsuario.equalsIgnoreCase("Administrador")) {
                        JOptionPane.showMessageDialog(null, "Sucesso");
                        AplicacaoBancaria.menuPrincipalAdministrador((Administrador) encontrado);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciais inválidas para este tipo de Usuário");
                    }
                }
            }
        } else if (login == 3) {
            JOptionPane.showMessageDialog(null, "Voltando...");
        } else {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
        }
    }
    private static void Cadastro() {
        String stringCadastro = JOptionPane.showInputDialog("Qual perfil deseja-se criar??\n" +
                "1 - Perfil para cliente\n" +
                "2 - Perfil para Administrador\n" +
                "3 - Voltar");

        if (stringCadastro == null) return;

        int cadastro;
        try {
            cadastro = Integer.parseInt(stringCadastro);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
            return;
        }

        if (cadastro == 1) {
            while (true) {
                String[] dados = SwingUtil.exibirFormularioCadastroCliente();
                if (dados == null) { System.out.println("Cadastro cancelado."); break; }

                String nome = dados[0];
                String cpf = dados[1];
                String telefone = dados[2];
                String dataNascimento = dados[3];
                String email = dados[4];
                String senha = dados[5];

                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                    Cliente cliente = new Cliente(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Cliente", Status.ATIVO);
                    AplicacaoBancaria.ListaUsuarios.add(cliente);
                    JOptionPane.showMessageDialog(null, "Cliente criado com sucesso!");
                    break;
                }
            }
        } else if (cadastro == 2) {
            while (true) {
                String[] dados = SwingUtil.exibirFormularioCadastroADM();
                if (dados == null) { System.out.println("Cadastro cancelado."); break; }

                String nome = dados[0];
                String cpf = dados[1];
                String telefone = dados[2];
                String dataNascimento = dados[3];
                String email = dados[4];
                String senha = dados[5];
                String matricula = dados[6];

                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty() || matricula.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                    Administrador administrador = new Administrador(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Administrador", matricula);
                    ListaUsuarios.add(administrador);
                    JOptionPane.showMessageDialog(null, "Administrador criado com sucesso!");
                    break;
                }
            }
        } else if (cadastro == 3) {
            JOptionPane.showMessageDialog(null, "Voltando...");
        } else {
            JOptionPane.showMessageDialog(null, "Opção inválida!");
        }
    }

    private static void AberturaConta(Cliente encontrado) {
        String tipo = SwingUtil.exibirSeletorTipoConta();
        if (tipo == null) return;

        String[] dados = SwingUtil.exibirFormularioConta(tipo);
        if (dados == null) return;

        try {
            String num = dados[0];
            String ag = dados[1];
            String saldoStr = dados[2];

            if (num.isEmpty() || ag.isEmpty() || saldoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Todos os campos básicos devem ser preenchidos.");
                return;
            }

            double sal = Double.parseDouble(saldoStr.replace(",", "."));

            if (tipo.equalsIgnoreCase("Conta Corrente")) {
                double limite = Double.parseDouble(dados[3].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, limite, 0, null, 0, null);

            } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                double taxa = Double.parseDouble(dados[3].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, 0, taxa, null, 0, null);

            } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                String cpfResp = dados[3];
                double limMes = Double.parseDouble(dados[4].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, 0, 0, cpfResp, limMes, null);

            } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                String risco = dados[3];
                encontrado.abrirConta(num, ag, sal, tipo, 0, 0, null, 0, risco);
            }

            JOptionPane.showMessageDialog(null, "Conta criada com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Verifique os campos numéricos.");
        }
    }

    public static void menuPrincipalAdministrador (Administrador encontrado) {}
}