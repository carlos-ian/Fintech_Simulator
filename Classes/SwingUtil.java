package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
        return null;
    }
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
    public static String exibirSeletorTipoConta() {
        String[] opcoes = {"Conta Corrente", "Conta Poupança", "Conta Kids", "Conta Investimento"};
        return (String) JOptionPane.showInputDialog(null, "Selecione o tipo de conta:",
                "Abertura de Conta", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }
    public static String[] exibirFormularioConta(String tipoConta) {
        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField txtNumero = new JTextField();
        JTextField txtAgencia = new JTextField();
        JTextField txtSaldo = new JTextField();

        painel.add(new JLabel("Número da Conta:")); painel.add(txtNumero);
        painel.add(new JLabel("Agência:")); painel.add(txtAgencia);
        painel.add(new JLabel("Saldo Inicial:")); painel.add(txtSaldo);

        // Campos específicos de cada tipo
        JTextField txtEspecifico1 = new JTextField();
        JTextField txtEspecifico2 = new JTextField();

        if (tipoConta.equals("Conta Corrente")) {
            painel.add(new JLabel("Limite Cheque Especial:"));
            painel.add(txtEspecifico1);
        } else if (tipoConta.equals("Conta Poupança")) {
            painel.add(new JLabel("Taxa de Rendimento:"));
            painel.add(txtEspecifico1);
        } else if (tipoConta.equals("Conta Kids")) {
            painel.add(new JLabel("CPF Responsável:"));
            painel.add(txtEspecifico1);
            painel.add(new JLabel("Limite Mensal:"));
            painel.add(txtEspecifico2);
        } else if (tipoConta.equals("Conta Investimento")) {
            painel.add(new JLabel("Perfil de Risco:"));
            painel.add(txtEspecifico1);
        }

        int confirmacao = JOptionPane.showConfirmDialog(null, painel, "Dados - " + tipoConta, JOptionPane.OK_CANCEL_OPTION);

        if (confirmacao == JOptionPane.OK_OPTION) {
            return new String[] {
                    txtNumero.getText(),
                    txtAgencia.getText(),
                    txtSaldo.getText(),
                    txtEspecifico1.getText(),
                    txtEspecifico2.getText()
            };
        }
        return null;
    }
    public static Object[] exibirFormularioTransacao(ArrayList<Cartao> cartoes) {
        JTextField txtValor = new JTextField();
        JComboBox<String> comboMetodo = new JComboBox<>(new String[]{"PIX", "DEBITO", "CREDITO"});
        JComboBox<String> comboCategoria = new JComboBox<>(new String[]{"Alimentação", "Lazer", "Saúde", "Educação", "Transporte", "Outros"});
        JTextField txtCpfDestino = new JTextField();
        JTextField txtNumContaDestino = new JTextField();

        String[] opcoes = new String[cartoes.size() + 1];
        opcoes[0] = "Não utilizar cartão (Saldo/PIX)";
        for (int i = 0; i < cartoes.size(); i++) {
            opcoes[i+1] = "Cartão Final: " + cartoes.get(i).getNumeroCartao().substring(Math.max(0, cartoes.get(i).getNumeroCartao().length() - 4));
        }
        JComboBox<String> comboCartoes = new JComboBox<>(opcoes);

        Object[] componentes = {
                "Valor:", txtValor,
                "Método:", comboMetodo,
                "Cartão:", comboCartoes,
                "Categoria:", comboCategoria,
                "\nDestinatário (Opcional):",
                "CPF:", txtCpfDestino,
                "Conta:", txtNumContaDestino
        };

        int res = JOptionPane.showConfirmDialog(null, componentes, "Transação", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            return new Object[]{
                    txtValor.getText(),
                    comboMetodo.getSelectedItem(),
                    comboCartoes.getSelectedIndex(),
                    comboCategoria.getSelectedItem(),
                    txtCpfDestino.getText(),
                    txtNumContaDestino.getText()
            };
        }
        return null;
    }
    public static String[] exibirFiltrosExtrato() {
        JComboBox<String> comboFluxo = new JComboBox<>(new String[]{"", "ENTRADA", "SAÍDA"});
        JComboBox<String> comboMetodo = new JComboBox<>(new String[]{"", "PIX", "DEBITO", "CREDITO"});
        JTextField txtCat = new JTextField();
        JTextField txtDias = new JTextField();

        Object[] componentes = {
                "Fluxo:", comboFluxo,
                "Método:", comboMetodo,
                "Categoria:", txtCat,
                "Dias (ex: 30):", txtDias
        };

        if (JOptionPane.showConfirmDialog(null, componentes, "Filtros", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            return new String[]{
                    (String)comboFluxo.getSelectedItem(),
                    (String)comboMetodo.getSelectedItem(),
                    txtCat.getText(),
                    txtDias.getText()
            };
        }
        return null;
    }
}
