package Classes;

import javax.swing.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class AplicacaoBancaria1 {
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

            if (stringMenu == null) {
                System.out.println("Sistema encerrado pelo usuário.");
                return;
            }

            try {
                menu = Integer.parseInt(stringMenu);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida! Digite apenas números.");
                menu = 0;
                continue;
            }

            if (menu == 1) {
                String stringLogin = JOptionPane.showInputDialog("Quem é você?\n" +
                        "1 - Sou Cliente\n" +
                        "2 - Sou Administrador\n" +
                        "3 - Voltar");

                if (stringLogin == null) {
                    menu = 0;
                    continue;
                }

                int login;
                try {
                    login = Integer.parseInt(stringLogin);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    menu = 0;
                    continue;
                }

                if (login == 1) {
                    JTextField cpfField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();

                    Object[] loginCliente = {
                            "Realizando Login...\n",
                            "CPF:", cpfField,
                            "Senha:", senhaField
                    };

                    while (login == 1) {
                        int opcao = JOptionPane.showConfirmDialog(null, loginCliente, "Cadastro", JOptionPane.OK_CANCEL_OPTION);

                        if (opcao == JOptionPane.OK_OPTION) {
                            String cpf = cpfField.getText();
                            String senha = new String(senhaField.getPassword());

                            if (cpf.isEmpty() || senha.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (Usuario.autenticar(cpf, senha)) {
                                    JOptionPane.showMessageDialog(null, "Sucesso");
                                    AplicacaoBancaria1.menuPrincipalCliente();
                                    menu = 0;
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(null, "Senha/CPF Inválidos, Try again");
                                }
                            }
                        } else {
                            System.out.println("Login cancelado.");
                            menu = 0;
                            break;
                        }
                    }

                } else if (login == 2) {
                    JTextField cpfField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();

                    Object[] loginADM = {
                            "Realizando Login...\n",
                            "CPF:", cpfField,
                            "Senha:", senhaField
                    };

                    while (login == 2) {
                        int opcao = JOptionPane.showConfirmDialog(null, loginADM, "Cadastro", JOptionPane.OK_CANCEL_OPTION);

                        if (opcao == JOptionPane.OK_OPTION) {
                            String cpf = cpfField.getText();
                            String senha = new String(senhaField.getPassword());

                            if (cpf.isEmpty() || senha.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (Usuario.autenticar(cpf, senha)) {
                                    JOptionPane.showMessageDialog(null, "Sucesso");
                                    AplicacaoBancaria1.menuPrincipalAdministrador();
                                    menu = 0;
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(null, "Senha/CPF Inválidos, Try again");
                                }
                            }
                        } else {
                            System.out.println("Login cancelado.");
                            menu = 0;
                            break;
                        }
                    }

                } else if (login == 3) {
                    JOptionPane.showMessageDialog(null, "Voltando...");
                    menu = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    menu = 0;
                }

            } else if (menu == 2) {
                String stringCadastro = JOptionPane.showInputDialog("Qual perfil deseja-se criar??\n" +
                        "1 - Perfil para cliente\n" +
                        "2 - Perfil para Administrador\n" +
                        "3 - Voltar");

                if (stringCadastro == null) {
                    menu = 0;
                    continue;
                }

                int cadastro;
                try {
                    cadastro = Integer.parseInt(stringCadastro);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    menu = 0;
                    continue;
                }

                if (cadastro == 1) {
                    JTextField nomeField = new JTextField();
                    JTextField cpfField = new JTextField();
                    JTextField telefoneField = new JTextField();
                    JTextField emailField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();
                    JTextField dataNascimentoField = new JTextField();

                    Object[] mensagem = {
                            "Vamos Criar sua conta, por favor preencha os campos:\n",
                            "Nome: ", nomeField,
                            "CPF: ", cpfField,
                            "Telefone: ", telefoneField,
                            "Data de Nascimento: ", dataNascimentoField,
                            "E-mail: ", emailField,
                            "Senha: ", senhaField
                    };

                    while (cadastro == 1) {
                        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Cadastro", JOptionPane.OK_CANCEL_OPTION);

                        if (opcao == JOptionPane.OK_OPTION) {
                            String nome = nomeField.getText();
                            String cpf = cpfField.getText();
                            String telefone = telefoneField.getText();
                            String dataNascimento = dataNascimentoField.getText();
                            String email = emailField.getText();
                            String senha = new String(senhaField.getPassword());
                            String tipoUsuario = "Cliente";
                            Status statusCliente = Status.ATIVO;

                            if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                            } else {
                                String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                                Cliente cliente = new Cliente(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, tipoUsuario, statusCliente);
                                AplicacaoBancaria1.ListaUsuarios.add(cliente);
                                cadastro = 0;
                                menu = 0;
                                break;
                            }
                        } else {
                            System.out.println("Cadastro cancelado.");
                            cadastro = 0;
                            menu = 0;
                        }
                    }

                } else if (cadastro == 2) {
                    JTextField nomeField = new JTextField();
                    JTextField cpfField = new JTextField();
                    JTextField telefoneField = new JTextField();
                    JTextField emailField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();
                    JTextField dataNascimentoField = new JTextField();
                    JTextField matriculaField = new JTextField();

                    Object[] mensagem = {
                            "Vamos Criar sua conta, por favor preencha os campos:\n",
                            "Nome: ", nomeField,
                            "CPF: ", cpfField,
                            "Telefone: ", telefoneField,
                            "Data de Nascimento: ", dataNascimentoField,
                            "E-mail: ", emailField,
                            "Senha: ", senhaField,
                            "Matrícula: ", matriculaField
                    };

                    while (cadastro == 2) {
                        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Cadastro", JOptionPane.OK_CANCEL_OPTION);

                        if (opcao == JOptionPane.OK_OPTION) {
                            String nome = nomeField.getText();
                            String cpf = cpfField.getText();
                            String telefone = telefoneField.getText();
                            String dataNascimento = dataNascimentoField.getText();
                            String email = emailField.getText();
                            String senha = new String(senhaField.getPassword());
                            String tipoUsuario = "Administrador";
                            String matricula = matriculaField.getText();

                            if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty() || matricula.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                            } else {
                                String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                                Administrador administrador = new Administrador(nome, cpf, email, senhaCriptografada, dataNascimento, telefone, tipoUsuario, matricula);
                                ListaUsuarios.add(administrador);
                                cadastro = 0;
                                menu = 0;
                                break;
                            }
                        } else {
                            System.out.println("Cadastro cancelado.");
                            cadastro = 0;
                            menu = 0;
                        }
                    }

                } else if (cadastro == 3) {
                    JOptionPane.showMessageDialog(null, "Voltando...");
                    menu = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    menu = 0;
                }

            } else if (menu == 3) {
                JOptionPane.showMessageDialog(null, "...ENCERRANDO O SISTEMA...");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "Opção inválida!");
                menu = 0;
            }
        }
    }

    public static void menuPrincipalCliente () {

        int mpC = 0;
        mpC = Integer.parseInt(JOptionPane.showInputDialog("1 - Configuração ou Perfil\n" +
                "2 - Realizar Transação\n" +
                "3 - Visualizar Extrato\n" +
                "4 - Gestão de Cartões\n" +
                "5 - Investimentos e Poupança\n" +
                "6 - Sair"));

        // Opção 1 -> Leva ao menu de configuracoes (dessa classe)

        // Opção 2 -> Coleta informações necessárias para realizar transação e chama metodo de
        // realizar transação da classe Conta (OBS: uma conta instaciada deve chamar esse metodo,
        // ou seja, conta1.realizarTranscao(), logo, pergunte qual conta da lista será utilizada
        // Para isso, pode chamar um metodo de listar contas

        // Opção 3 -> Apenas chama metodo de visualizar extrato, de modo que a conta instaciada
        // que deve também chamar esse metodo

        // OBS: Para facilitar, se quiser, no menu principal de cliente liste todas as contas
        // do cliente e pergunte qual será usada, permitindo voltar e trocar a conta com alguma opção
        // Creio que assim é mais fácil

        // Opção 4 -> Leva ao menu de Gestão de Cartões (dessa classe)

        // Opção 5 -> Leva ao menu de Investimentos e Poupança (dessa classe)

        // Opção 6 -> Volta para Menu Inicial de Login
    }

    public static void menuPrincipalAdministrador () {

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