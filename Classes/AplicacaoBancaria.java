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

            if (stringMenu == null) { System.out.println("Sistema encerrado pelo usuário."); return; }

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
                        StringBuilder sb = new StringBuilder("=== SUAS CONTAS BANCÁRIAS ===\n\n");
                        for (Conta c : contas) {
                            sb.append(c.visualizarDadosConta()).append("\n-----------------------\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Contas", JOptionPane.INFORMATION_MESSAGE);
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

                    String[] opcoesFechar = new String[contas.size()];
                    for (int i = 0; i < contas.size(); i++) {
                        opcoesFechar[i] = "ID: " + i + " | Conta: " + contas.get(i).getNumeroConta() + " [" + contas.get(i).getTipoConta() + "]";
                    }

                    String contaFecharSel = SwingUtil.exibirMenuSelecao("Encerrar Conta", "Selecione a conta que deseja fechar permanentemente:", opcoesFechar);
                    if (contaFecharSel == null) { mpC = 0; break; }

                    try {
                        int id = Integer.parseInt(contaFecharSel.split(" ")[1]);
                        boolean resultado = encontrado.fecharConta(contas.get(id));

                        if (resultado) {
                            JOptionPane.showMessageDialog(null, "Conta encerrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Falha ao fechar conta (Verifique se há saldo residual pendente).", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Erro ao processar o encerramento.");
                    }
                    mpC = 0;
                    break;

                case 4:
                    if (contas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui contas. Abra uma primeiro.");
                        mpC = 0;
                        break;
                    }

                    String[] opcoesEntrar = new String[contas.size()];
                    for (int i = 0; i < contas.size(); i++) {
                        opcoesEntrar[i] = "ID: " + i + " | Conta: " + contas.get(i).getNumeroConta() + " [" + contas.get(i).getTipoConta() + "]";
                    }

                    String contaEntrarSel = SwingUtil.exibirMenuSelecao("Acessar Conta", "Escolha em qual conta deseja logar no Dashboard:", opcoesEntrar);
                    if (contaEntrarSel == null) { mpC = 0; break; }

                    try {
                        int id = Integer.parseInt(contaEntrarSel.split(" ")[1]);
                        Conta contaSelecionada = contas.get(id);
                        menuPrincipalCliente(contaSelecionada, encontrado);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Erro ao tentar acessar a conta selecionada.");
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
                        JOptionPane.showMessageDialog(null, "Perfil excluído com sucesso. Desconectando do sistema...", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    String tipoDado = SwingUtil.exibirMenuSelecao("Mapeador Cadastral", "Selecione o dado que deseja modificar:", "Email", "Senha", "Telefone");
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
                        JOptionPane.showMessageDialog(null, tipoDado + " atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3:
                    JOptionPane.showMessageDialog(null, encontrado.visualizarDados(), "Dados do Perfil", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 4:
                    String statusEscolhido = SwingUtil.exibirMenuSelecao("Chaveamento de Acesso", "Escolha o novo estado operacional do perfil:", "ATIVO", "INATIVO");
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
                            JOptionPane.showMessageDialog(null, "Como seu perfil foi inativado, você será desconectado do ecossistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
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

                String escolhaCartao = SwingUtil.exibirMenuSelecao("Seletor de Hardware", "Escolha o cartão para realizar a operação:", opcoesCartao);
                if (escolhaCartao == null) continue;

                int idCartao = Integer.parseInt(escolhaCartao.split(" ")[1]);
                cartaoSelecionado = listaCartoes.get(idCartao);
            }

            switch (opcaoCartao) {
                case 1:
                    String[] dadosNovoCartao = SwingUtil.exibirFormulario("Emissão de Novo Cartão", "Insira os parâmetros físicos da bandeira:", "Número do Cartão (16 dígitos):", "Tipo (DEBITO/CREDITO):", "Limite de Crédito:");
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
                    String[] novoLimiteForm = SwingUtil.exibirFormulario("Análise Score", "Solicitação de alteração de linha de crédito:", "Informe o novo limite total desejado:");
                    if (novoLimiteForm == null || novoLimiteForm[0].isEmpty()) break;

                    try {
                        double novoLimite = Double.parseDouble(novoLimiteForm[0].replace(",", "."));

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
                    String[] opcoesCat = new String[produtosDisponiveis.size()];
                    for (int i = 0; i < produtosDisponiveis.size(); i++) {
                        opcoesCat[i] = "ID: " + i + " | " + produtosDisponiveis.get(i).getNomeProduto() + " (" + produtosDisponiveis.get(i).getTaxaRendimento() + "% a.a.)";
                    }

                    String produtoEscolhido = SwingUtil.exibirMenuSelecao("Catálogo Comercial", "Selecione em qual título deseja aplicar o capital:", opcoesCat);
                    if (produtoEscolhido == null) break;

                    String[] valorForm = SwingUtil.exibirFormulario("Aporte Inicial", null, "Valor para Investir (R$):");
                    if (valorForm == null || valorForm[0].isEmpty()) break;

                    try {
                        int indexSelecionado = Integer.parseInt(produtoEscolhido.split(" ")[1]);
                        double valorInvestir = Double.parseDouble(valorForm[0].replace(",", "."));

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

                    String[] ativosParaResgate = new String[conta.listaInvestimentos.size()];
                    for (int i = 0; i < conta.listaInvestimentos.size(); i++) {
                        ativosParaResgate[i] = "ID: " + i + " | Ativo: " + conta.listaInvestimentos.get(i).getNomeProduto();
                    }

                    String resgateSel = SwingUtil.exibirMenuSelecao("Liquidação de Ativos", "Escolha qual aplicação deseja resgatar:", ativosParaResgate);
                    if (resgateSel == null) break;

                    try {
                        int idInvestimento = Integer.parseInt(resgateSel.split(" ")[1]);

                        if (helper.resgatarInvestimento(conta, idInvestimento)) {
                            JOptionPane.showMessageDialog(null, "Resgate concluído! O valor retornou ao seu saldo livre.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
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

    public static void menuPrincipalAdministrador(Administrador admin) {
        int mpA = 0;

        while (mpA != 5) {
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

                    int acaoCliente = SwingUtil.exibirMenuBotoes("Gerenciador Operacional de Clientes",
                            "Cliente: " + clienteEncontrado.getNome() + "\nStatus Atual: " + clienteEncontrado.getStatus(),
                            "Ativar Perfil", "Desativar Perfil", "Cancelar");

                    if (acaoCliente == 0) {
                        boolean mudou = admin.ativarPerfilCliente(clienteEncontrado);
                        JOptionPane.showMessageDialog(null, mudou ? "Perfil ativado com sucesso!" : "O perfil já estava ativo.");
                    } else if (acaoCliente == 1) {
                        boolean mudou = admin.desativarPerfilCliente(clienteEncontrado);
                        JOptionPane.showMessageDialog(null, mudou ? "Perfil desativado com sucesso!" : "O perfil já estava inativo.");
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

                    String contaSel = SwingUtil.exibirMenuSelecao("Mapeador de Contas", "Selecione qual conta auditar:", contasStr);
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
                        String justificativa = JOptionPane.showInputDialog("Digite a justificativa para o bloqueio:");
                        if (justificativa != null && !justificativa.isBlank()) {
                            boolean ok = admin.bloquearConta(contaAlvo, justificativa);
                            JOptionPane.showMessageDialog(null, ok ? "Conta bloqueada com sucesso." : "A conta já está bloqueada.");
                        }
                    } else if (acaoConta == 1) {
                        boolean ok = admin.desbloquearConta(contaAlvo);
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

                    for (Usuario u : AplicacaoBancaria.ListaUsuarios) {
                        if (u instanceof Cliente) {
                            Cliente c = (Cliente) u;
                            todosClientes.add(c);
                            for (Conta con : c.obterContas()) {
                                todasContas.add(con);
                                if (con.extrato != null) {
                                    todasTransacoes.addAll(con.extrato);
                                }
                            }
                        }
                    }

                    String relatorio = admin.gerarRelatorioFintech(todosClientes, todasContas, todasTransacoes);

                    JTextArea txtRelatorio = new JTextArea(18, 50);
                    txtRelatorio.setText(relatorio);
                    txtRelatorio.setEditable(false);
                    txtRelatorio.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

                    JOptionPane.showMessageDialog(null, new JScrollPane(txtRelatorio), "Fintech Analytics System", JOptionPane.PLAIN_MESSAGE);
                    break;

                case 5:
                    JOptionPane.showMessageDialog(null, "Efetuando logoff administrativo...");
                    return;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }

    private static void Login() {
        String loginTipo = SwingUtil.exibirMenuSelecao("Identificação de Login", "Quem é você no sistema?", "Sou Cliente", "Sou Administrador");
        if (loginTipo == null) return;

        if (loginTipo.equals("Sou Cliente")) {
            while (true) {
                String[] dados = SwingUtil.exibirFormulario("Autenticação de Segurança", "Entre com as suas credenciais de acesso:", "CPF:", "Senha:");
                if (dados == null) { System.out.println("Login cancelado."); break; }

                String cpf = dados[0].trim();
                String senha = dados[1].trim();

                if (cpf.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    Usuario encontrado = Usuario.autenticar(cpf, senha);
                    if (encontrado == null) {
                        JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (encontrado.tipoUsuario.equalsIgnoreCase("Cliente")) {
                        JOptionPane.showMessageDialog(null, "Autenticação Concluída com Sucesso!");
                        AplicacaoBancaria.menuGestaoContas((Cliente) encontrado);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciais inválidas para este tipo de Usuário", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else if (loginTipo.equals("Sou Administrador")) {
            while (true) {
                String[] dados = SwingUtil.exibirFormulario("Painel Root - Autenticação", "Credenciais estritas do setor operacional:", "CPF ADM:", "Senha ADM:");
                if (dados == null) { System.out.println("Login cancelado."); break; }

                String cpf = dados[0].trim();
                String senha = dados[1].trim();

                if (cpf.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    Usuario encontrado = Usuario.autenticar(cpf, senha);
                    if (encontrado == null) {
                        JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (encontrado.tipoUsuario.equalsIgnoreCase("Administrador")) {
                        JOptionPane.showMessageDialog(null, "Autenticação Administrativa Concedida.");
                        AplicacaoBancaria.menuPrincipalAdministrador((Administrador) encontrado);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciais inválidas para este perfil corporativo", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private static void Cadastro() {
        String tipoCadastro = SwingUtil.exibirMenuSelecao("Tipo de Perfil", "Qual perfil deseja-se instanciar?", "Perfil para Cliente", "Perfil para Administrador");
        if (tipoCadastro == null) return;

        if (tipoCadastro.equals("Perfil para Cliente")) {
            while (true) {
                String[] dados = SwingUtil.exibirFormulario("Cadastro de Cliente", "Insira suas informações pessoais nos campos abaixo:", "Nome:", "CPF:", "Telefone:", "Data de Nascimento (dd/mm/aaaa):", "E-mail:", "Senha:");
                if (dados == null) { System.out.println("Cadastro cancelado."); break; }

                String nome = dados[0].trim();
                String cpf = dados[1].trim();
                String telefone = dados[2].trim();
                String dataNascimento = dados[3].trim();
                String email = dados[4].trim();
                String senha = dados[5].trim();

                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos mandatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                    Cliente cliente = new Cliente(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Cliente");
                    AplicacaoBancaria.ListaUsuarios.add(cliente);
                    JOptionPane.showMessageDialog(null, "Cliente registrado com sucesso no banco de dados!");
                    break;
                }
            }
        } else if (tipoCadastro.equals("Perfil para Administrador")) {
            while (true) {
                String[] dados = SwingUtil.exibirFormulario("Cadastro de Administrador", "Insira os privilégios e dados administrativos:", "Nome:", "CPF:", "Telefone:", "Data de Nascimento (dd/mm/aaaa):", "E-mail:", "Senha:", "Matrícula:");
                if (dados == null) { System.out.println("Cadastro cancelado."); break; }

                String nome = dados[0].trim();
                String cpf = dados[1].trim();
                String telefone = dados[2].trim();
                String dataNascimento = dados[3].trim();
                String email = dados[4].trim();
                String senha = dados[5].trim();
                String matricula = dados[6].trim();

                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty() || matricula.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos mandatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                    Administrador administrador = new Administrador(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, "Administrador", matricula);
                    ListaUsuarios.add(administrador);
                    JOptionPane.showMessageDialog(null, "Administrador registrado e homologado com sucesso!");
                    break;
                }
            }
        }
    }

    private static void AberturaConta(Cliente encontrado) {
        String tipo = SwingUtil.exibirMenuSelecao("Abertura de Conta", "Selecione o tipo de conta ideal para seu perfil corporativo:", "Conta Corrente", "Conta Poupança", "Conta Kids", "Conta Investimento");
        if (tipo == null) return;

        String[] dados;
        switch (tipo) {
            case "Conta Corrente":
                dados = SwingUtil.exibirFormulario("Nova Conta Corrente", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "Limite Cheque Especial:");
                break;
            case "Conta Poupança":
                dados = SwingUtil.exibirFormulario("Nova Conta Poupança", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "Taxa de Rendimento Mensal:");
                break;
            case "Conta Kids":
                dados = SwingUtil.exibirFormulario("Nova Conta Kids", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "CPF do Responsável Legal:", "Limite de Movimentação Mensal:");
                break;
            default:
                dados = SwingUtil.exibirFormulario("Nova Conta Investimento", null, "Número da Conta:", "Agência:", "Saldo Inicial:", "Perfil de Risco (Baixo/Medio/Alto):");
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

            if (tipo.equalsIgnoreCase("Conta Corrente")) {
                double limite = Double.parseDouble(dados[3].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, limite, 0, null, 0, null);

            } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                double taxa = Double.parseDouble(dados[3].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, 0, taxa, null, 0, null);

            } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                String cpfResp = dados[3].trim();
                double limMes = Double.parseDouble(dados[4].replace(",", "."));
                encontrado.abrirConta(num, ag, sal, tipo, 0, 0, cpfResp, limMes, null);

            } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                String risco = dados[3].trim();
                encontrado.abrirConta(num, ag, sal, tipo, 0, 0, null, 0, risco);
            }

            JOptionPane.showMessageDialog(null, "Conta bancária criada e vinculada com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Verifique a formatação dos campos numéricos.");
        }
    }

    // Método ponte criado para manter a compatibilidade com a assinatura de chamada do menu principal
    public static void menuGestaoContas(Cliente encontrado) {
        menuGestaoCartoes(encontrado);
    }
}