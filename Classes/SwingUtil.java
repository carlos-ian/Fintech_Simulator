package Classes;

import javax.swing.*;
import java.awt.*;

public class SwingUtil {

    public static String[] exibirFormularioLogin() {
        JTextField cpfField = new JTextField();
        JPasswordField senhaField = new JPasswordField();

        Object[] componentes = {
                "Realizando Login...\n",
                "CPF:", cpfField,
                "Senha:", senhaField
        };

        int opcao = JOptionPane.showConfirmDialog(null, componentes, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            return new String[]{ cpfField.getText(), new String(senhaField.getPassword()) };
        }
        return null; // Cancelou ou fechou a janela
    }

    // Método para exibir o formulário do Cliente
    public static String[] exibirFormularioCadastroCliente() {
        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField telefoneField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JTextField dataNascimentoField = new JTextField();

        Object[] componentes = {
                "Vamos Criar sua conta, por favor preencha os campos:\n",
                "Nome: ", nomeField,
                "CPF: ", cpfField,
                "Telefone: ", telefoneField,
                "Data de Nascimento: ", dataNascimentoField,
                "E-mail: ", emailField,
                "Senha: ", senhaField
        };

        int opcao = JOptionPane.showConfirmDialog(null, componentes, "Cadastro Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            return new String[]{
                    nomeField.getText(),
                    cpfField.getText(),
                    telefoneField.getText(),
                    dataNascimentoField.getText(),
                    emailField.getText(),
                    new String(senhaField.getPassword())
            };
        }
        return null;
    }

    // Método para exibir o formulário do Administrador
    public static String[] exibirFormularioCadastroADM() {
        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField telefoneField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JTextField dataNascimentoField = new JTextField();
        JTextField matriculaField = new JTextField();

        Object[] componentes = {
                "Vamos Criar sua conta, por favor preencha os campos:\n",
                "Nome: ", nomeField,
                "CPF: ", cpfField,
                "Telefone: ", telefoneField,
                "Data de Nascimento: ", dataNascimentoField,
                "E-mail: ", emailField,
                "Senha: ", senhaField,
                "Matrícula: ", matriculaField
        };

        int opcao = JOptionPane.showConfirmDialog(null, componentes, "Cadastro Administrador", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            return new String[]{
                    nomeField.getText(),
                    cpfField.getText(),
                    telefoneField.getText(),
                    dataNascimentoField.getText(),
                    emailField.getText(),
                    new String(senhaField.getPassword()),
                    matriculaField.getText()
            };
        }
        return null;
    }
}
