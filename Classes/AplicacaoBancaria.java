package Classes;

import javax.swing.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class AplicacaoBancaria {
    public static ArrayList<Usuario> ListaUsuarios = new ArrayList<>();
    public static ArrayList<Investimento> produtosDisponiveis = new ArrayList<>();

    public static void main(String[] args) {
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
                    menuLogin();
                    menu = 0;
                    break;
                case 2:
                    menuCadastro();
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

    private static void menuLogin() {
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
                        AplicacaoBancaria.menuPrincipalCliente((Cliente) encontrado);
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
    private static void menuCadastro() {
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






    public static void menuPrincipalCliente (Cliente encontrado) {}
    public static void menuPrincipalAdministrador (Administrador encontrado) {}
}


















/*
    public static void menuPrincipalCliente (Cliente encontrado) {

        int mpC = 0;

        while (mpC == 0) {

        mpC = Integer.parseInt(JOptionPane.showInputDialog("1 - Listar Contas\n" +
                "2 - Abrir Nova Conta\n" +
                "3 - Fechar Conta\n" +
                "4 - Entrar com Conta\n" +
                "5 - Sair"));

        if (mpC == 1) {
            ArrayList<Conta> Contas = encontrado.obterContas();
            for (Conta c : Contas) {
                String x = c.visualizarDadosConta();
                JOptionPane.showMessageDialog(null, x);
            }

        } else if (mpC == 2) {
            String[] opcoes = {"Conta Corrente", "Conta Poupança", "Conta Kids", "Conta Investimento"};
            String tipo = (String) JOptionPane.showInputDialog(null, "Selecione o tipo de conta:",
                    "Abertura de Conta", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

            if (tipo == null) return;

            JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));

            JTextField txtNumero = new JTextField();
            JTextField txtAgencia = new JTextField();
            JTextField txtSaldo = new JTextField();

            painel.add(new JLabel("Número da Conta:")); painel.add(txtNumero);
            painel.add(new JLabel("Agência:")); painel.add(txtAgencia);
            painel.add(new JLabel("Saldo Inicial:")); painel.add(txtSaldo);

            JTextField txtEspecial = new JTextField();
            JTextField txtTaxa = new JTextField();
            JTextField txtCPFResp = new JTextField();
            JTextField txtLimiteMes = new JTextField();
            JTextField txtRisco = new JTextField();

            if (tipo.equals("Conta Corrente")) {
                painel.add(new JLabel("Limite Cheque Especial:"));
                painel.add(txtEspecial);
            } else if (tipo.equals("Conta Poupança")) {
                painel.add(new JLabel("Taxa de Rendimento:"));
                painel.add(txtTaxa);
            } else if (tipo.equals("Conta Kids")) {
                painel.add(new JLabel("CPF Responsável:"));
                painel.add(txtCPFResp);
                painel.add(new JLabel("Limite Mensal:"));
                painel.add(txtLimiteMes);
            } else if (tipo.equals("Conta Investimento")) {
                painel.add(new JLabel("Perfil de Risco:"));
                painel.add(txtRisco);
            }

            int confirmacao = JOptionPane.showConfirmDialog(null, painel,
                    "Dados - " + tipo, JOptionPane.OK_CANCEL_OPTION);

            if (confirmacao == JOptionPane.OK_OPTION) {
                try {
                    String num = txtNumero.getText();
                    String ag = txtAgencia.getText();
                    double sal = Double.parseDouble(txtSaldo.getText().replace(",", "."));


                    if (tipo.equalsIgnoreCase("Conta Corrente")) {
                        double limite = Double.parseDouble(txtEspecial.getText().replace(",", "."));
                        encontrado.abrirConta(num, ag, sal, tipo, limite, 0, null,  0, null);

                    } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                        double taxa = Double.parseDouble(txtTaxa.getText().replace(",", "."));
                        encontrado.abrirConta(num, ag, sal, tipo, 0, taxa, null,  0, null);

                    } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                        String cpf = txtCPFResp.getText();
                        double limMes = Double.parseDouble(txtLimiteMes.getText().replace(",", "."));
                        encontrado.abrirConta(num, ag, sal, tipo, 0, 0, cpf,  limMes, null);

                    } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                        String risco = txtRisco.getText();
                        encontrado.abrirConta(num, ag, sal, tipo, 0, 0, null, 0, risco);
                    }

                    JOptionPane.showMessageDialog(null, "Conta criada com sucesso!");

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Erro: Verifique os campos numéricos.");
                }
            }

        } else if (mpC == 3) {

            ArrayList<Conta> Contas = encontrado.obterContas();

            for (Conta c : Contas) {
                String x = c.visualizarDadosConta();
                JOptionPane.showMessageDialog(null, x);
            }

            int id;

            id = Integer.parseInt(JOptionPane.showInputDialog("Qual o id da conta que deseja-se excluir?"));
            boolean resultado = encontrado.fecharConta(Contas.get(id));

            if (resultado) {
                JOptionPane.showMessageDialog(null, "Sucesso");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao remover");
            }

        } else if (mpC == 4) {

            ArrayList<Conta> Contas = encontrado.obterContas();

            for (Conta c : Contas) {
                String x = c.visualizarDadosConta();
                JOptionPane.showMessageDialog(null, x);
            }

            int id;

            id = Integer.parseInt(JOptionPane.showInputDialog("Qual o id da conta que deseja-se entrar?"));
            Conta conta = Contas.get(id);
            dashboardCliente(conta, encontrado);

        } else if (mpC == 5) {
            return;
        }


        }

    }
    public static void dashboardCliente (Conta conta, Cliente encontrado) {

    }

    public static void menuPrincipalAdministrador (Administrador encontrado) {

        int mpADM = 0;

        mpADM = Integer.parseInt(JOptionPane.showInputDialog("1- Configuração ou Perfil\n" +
                "2 - Consultar Cliente\n" +
                "3 - Analisar Solicitações de Ajuste de Limite\n" +
                "4 - Manutenção de Investimentos\n" +
                "5 - Relatório Geral\n" +
                "6 - Sair"));

        // Opção 1 -> Leva ao menu de configuracoes (dessa classe)

        // Opção 2 -> Leva ao menu de consultarCliente (dessa classe)

        // Opção 3 -> Chama metodo de Analisar Solicitacoes de Ajuste de Limite

        // Opção 4 -> Leva ao menu de manutencaInvestimentos (dessa classe)

        // Opção 5 -> Chama metodo de Relatorio Geral

        // Opção 6 -> Volta para Menu Inicial de Login
    }

    public void configuracoes () {

        int config =0;
        Integer.parseInt(JOptionPane.showInputDialog("1 - Excluir Perfil\n" +
                "2 - Alterar Dados do Perfil\n" +
                "3 - Visualizar Dados do Perfil\n" +
                "4 - Voltar"));

        // Opção 1 -> Chama metodo de Excluir Perfil
        // Opção 2 -> Chama metodo de Alterar Dados
        // Opção 3 -> Chama metodo de Visualizar Dados

    }
}
 */