package Classes;

import javax.swing.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class AplicacaoBancaria1 {
        public static ArrayList<Usuario> ListaUsuarios = new ArrayList<>();
    public static void main(String[] args) {

        // Opções:
        // 1. Fazer Login
        // 2. Criar Nova Conta de Usuário
        // 3. Sair

        int menu = 0;

        while (menu == 0) {
            menu = Integer.parseInt(JOptionPane.showInputDialog
                    ("===============================\n" +
                            "SEJA BEM VINDO AO NOSSO SISTEMA\n" +
                            "===============================\n" +
                            "1 - Fazer Login\n" +
                            "2 - Criar uma conta\n" +
                            "3 - Sair\n"));

            if (menu == 1) {
                int login;

                login = Integer.parseInt(JOptionPane.showInputDialog("Quem é você?\n" +
                        "1 - Sou Cliente\n" +
                        "2 - Sou Administrador\n" +
                        "3 - Voltar"));

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
                        String senha = senhaField.getText();

                        if (cpf.isEmpty() || senha.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Realizar autenticação + levar ao menu

                            if (Usuario.autenticar(cpf, senha)){
                                JOptionPane.showMessageDialog(null, "Sucesso");

                                AplicacaoBancaria1.menuPrincipalCliente();

                            } else {
                                JOptionPane.showMessageDialog(null, "Senha/CPF Inválidos, Try again");
                            }

                            }

                        } else {

                        System.out.println("Login cancelado.");
                        menu = 0;

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
                            String senha = senhaField.getText();

                            if (cpf.isEmpty() || senha.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Realizar autenticação + levar ao menu

                                if (Usuario.autenticar(cpf, senha)){
                                    JOptionPane.showMessageDialog(null, "Sucesso");
                                    AplicacaoBancaria1.menuPrincipalAdministrador();

                                } else {
                                    JOptionPane.showMessageDialog(null, "Senha/CPF Inválidos, Try again");
                                }
                            }
                        } else {

                            System.out.println("Login cancelado.");
                            menu = 0;

                        }
                    }

                } else if (login == 3) {
                    JOptionPane.showMessageDialog(null, "Voltando...");
                    menu = 0;

                }

            } else if (menu == 2) {
                int cadastro;

                cadastro = Integer.parseInt(JOptionPane.showInputDialog("Qual perfil deseja-se criar??\n" +
                        "1 - Perfil para cliente\n" +
                        "2 - Perfil para Administrador\n" +
                        "3 - Voltar"));

                if (cadastro == 1) {

                    JTextField nomeField = new JTextField();
                    JTextField cpfField = new JTextField();
                    JTextField telefoneField = new JTextField();
                    JTextField emailField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();
                    JTextField dataNascimentoField = new JTextField();

                    Object[] mensagem = {
                            "Vamos Criar sua conta, porfavor preencha os campos:\n",
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
                            String senha = new String(senhaField.getPassword()); // Senhas são char[] por segurança
                            String tipoUsuario = "Cliente";
                            Status statusCliente = Status.ATIVO;

                            if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);

                            } else {
                                String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                                Cliente cliente = new Cliente(nome, cpf, telefone, dataNascimento, email, senhaCriptografada, tipoUsuario, statusCliente);
                                AplicacaoBancaria1.ListaUsuarios.add(cliente);
                                cadastro = 0;
                                menu = 0;

                                continue;
                            }

                        } else {
                            System.out.println("Cadastro cancelado.");
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
                            "Vamos Criar sua conta, porfavor preencha os campos:\n",
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
                            String senha = new String(senhaField.getPassword()); // Senhas são char[] por segurança
                            String tipoUsuario = "Cliente";
                            String matricula = matriculaField.getText();

                            if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || senha.isEmpty() || matricula.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Não foram preenchidos todos os campos", "Erro", JOptionPane.ERROR_MESSAGE);

                            } else {
                                String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt(12));
                                Administrador administrador = new Administrador(nome, cpf, telefone, dataNascimento, email, senhaCriptografada, tipoUsuario, matricula);
                                ListaUsuarios.add(administrador);
                                cadastro = 0;
                                menu = 0;

                                continue;
                            }

                        } else {
                            System.out.println("Cadastro cancelado.");
                            menu = 0;

                        }
                    }

                } else if (cadastro == 3) {
                    JOptionPane.showMessageDialog(null, "Voltando...");
                    menu =0;
                }

            } else if (menu == 3) {
                JOptionPane.showMessageDialog(null, "...ENCERRANDO O SISTEMA...");
                return;
            }
                }
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

            public static void menuPrincipalCliente () {
                // Menu Principal com Funcionalidades do Cliente

                int mpC = 0;
                mpC = Integer.parseInt(JOptionPane.showInputDialog("1 - Configuração ou Perfil\n" +
                        "2 - Realizar Transação\n" +
                        "3 - Visualizar Extrato\n" +
                        "4 - Gestão de Cartões\n" +
                        "5 - Investimentos e Poupança\n" +
                        "6 - Sair"));
            }

            public static void menuPrincipalAdministrador () {

                int mpADM = 0;

                mpADM = Integer.parseInt(JOptionPane.showInputDialog("1- Configuração ou Perfil\n" +
                        "2 - Consultar Cliente\n" +
                        "3 - Analisar Solicitações de Ajuste de Limite\n" +
                        "4 - Manutenção de Investimentos\n" +
                        "5 - Relatório Geral\n" +
                        "6 - Sair"));

            } // Menu Principal com Funcionalidades do Administrador

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

            public void configuracoes () {

            int config =0;

            Integer.parseInt(JOptionPane.showInputDialog("1 - Excluir Perfil\n" +
                    "2 - Alterar Dados do Perfil\n" +
                    "3 - Visualizar Dados do Perfil\n" +
                    "4 - Voltar"));

            } // Menu de Configurações do Cliente

            // Opções:
            // 1. Excluir Perfil
            // 3. Alterar Dados do Perfil
            // 4. Visualizar Dados do Perfil
            // 5. Voltar
            // Para cada opção, colete os dados necessarios e chame os metodos necessarios
            // Mostre mensagem de confirmacao da operacao no final
        }


