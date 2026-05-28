package Classes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.ArrayList;

public class AplicacaoBancaria {
    public static ArrayList<Investimento> produtosDisponiveis;

    public static void main(String[] args) {

        int opcao1 = 0;

        while (opcao1 == 0) {

            opcao1 = Integer.parseInt(JOptionPane.showInputDialog
                    ("===============================\n" +
                    "SEJA BEM VINDO AO NOSSO SISTEMA\n" +
                    "===============================\n" +
                    "\nQuem é você?\n" +
                    "1 - Sou Cliente\n" +
                    "2 - Sou Administrador\n" +
                    "3 - Ainda não possuo um cadastro\n"));

            if (opcao1 == 1) {

                String email = JOptionPane.showInputDialog("Vamos realizar o seu Login...\n" +
                        "Porfavor, insira o seu email:\n");
                String senha = JOptionPane.showInputDialog("Agora, insira sua senha");

            } else if (opcao1 == 2) {

                String email = JOptionPane.showInputDialog("Vamos realizar o seu Login...\n" +
                        "Porfavor, insira o seu email:\n");
                String senha = JOptionPane.showInputDialog("Agora, insira sua senha");

            } else if (opcao1 == 3) {
                ArrayList<Cliente> ListaClientes = new ArrayList<>();

                    // 1. Criar os componentes de interface

                    JTextField nomeField = new JTextField();
                    JTextField cpfField = new JTextField();
                    JTextField telefoneField = new JTextField();
                    JTextField emailField = new JTextField();
                    JPasswordField senhaField = new JPasswordField();
                    JTextField dataNascimentoField = new JTextField();


                    // 2. Organizar os componentes em um array de objetos

                    Object[] mensagem = {
                            "Vamos Criar sua conta, porfavor preencha os campos:\n",
                            "Nome: ", nomeField,
                            "CPF: ", cpfField,
                            "Telefone: ", telefoneField,
                            "Data de Nascimento: ", dataNascimentoField,
                            "E-mail: ", emailField,
                            "Senha: ", senhaField
                    };

                    while (opcao1 == 3) {

                    int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Cadastro", JOptionPane.OK_CANCEL_OPTION);

                    // 4. Processar o resultado

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
                            Cliente cliente = new Cliente(nome, cpf, telefone, dataNascimento, email, senha, tipoUsuario, statusCliente);
                            ListaClientes.add(cliente);
                            opcao1 = 0;

                            continue;
                        }
                /*
                CONFERIR OS DADOS
                System.out.println("Nome: " + nome);
                System.out.println("CPF: " + cpf);
                System.out.println("Telefone: " + telefone);
                System.out.println("Data de Nascimeto (dd/mm/aaaa): " + dataNascimento);
                System.out.println("Email: " + email);
                System.out.println("Senha: " + senha);
                */

                    } else {
                        System.out.println("Login cancelado.");
                        opcao1 = 0;

                    }
                }
            }
        }
    }
}