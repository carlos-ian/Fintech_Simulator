package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwingUtil {
    public static String[] exibirFormulario(String titulo, String mensagem, String... campos) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        if (mensagem != null && !mensagem.isEmpty()) {
            JLabel lblMensagem = new JLabel(mensagem);
            lblMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelPrincipal.add(lblMensagem);
            painelPrincipal.add(Box.createVerticalStrut(10));
        }

        JTextField[] textFields = new JTextField[campos.length];

        for (int i = 0; i < campos.length; i++) {
            JLabel label = new JLabel(campos[i]);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (campos[i].toLowerCase().contains("senha")) {
                textFields[i] = new JPasswordField(20);
            } else {
                textFields[i] = new JTextField(20);
            }
            textFields[i].setAlignmentX(Component.LEFT_ALIGNMENT);

            painelPrincipal.add(label);
            painelPrincipal.add(Box.createVerticalStrut(2));
            painelPrincipal.add(textFields[i]);
            painelPrincipal.add(Box.createVerticalStrut(8));
        }

        int resultado = JOptionPane.showConfirmDialog(null, painelPrincipal, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String[] dados = new String[campos.length];
            for (int i = 0; i < campos.length; i++) {
                if (textFields[i] instanceof JPasswordField) {
                    dados[i] = new String(((JPasswordField) textFields[i]).getPassword());
                } else {
                    dados[i] = textFields[i].getText();
                }
            }
            return dados;
        }

        return null;
    }

    public static String exibirMenuSelecao(String titulo, String mensagem, String... opcoes) {
        if (opcoes == null || opcoes.length == 0) return null;
        return (String) JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    public static int exibirMenuBotoes(String titulo, String mensagem, String... botoes) {
        return JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, botoes, botoes[0]);
    }

    public static Object[] exibirFormularioTransacao(ArrayList<Cartao> cartoes) {
        JTextField txtValor = new JTextField();
        JComboBox<String> comboMetodo = new JComboBox<>(new String[]{"PIX", "DEBITO", "CREDITO"});
        JComboBox<String> comboCat = new JComboBox<>(new String[]{"Alimentação", "Lazer", "Saúde", "Educação", "Transporte", "Outros"});
        JTextField txtCpf = new JTextField();
        JTextField txtConta = new JTextField();

        String[] opcoesCartoes = new String[cartoes.size() + 1];
        opcoesCartoes[0] = "Não utilizar cartão (Saldo/PIX)";
        for (int i = 0; i < cartoes.size(); i++) {
            String num = cartoes.get(i).getNumeroCartao();
            opcoesCartoes[i + 1] = "Cartão Final: " + (num.length() >= 4 ? num.substring(num.length() - 4) : num);
        }
        JComboBox<String> comboCartoes = new JComboBox<>(opcoesCartoes);

        Object[] componentes = {
                "Valor da Operação:", txtValor,
                "Forma de Pagamento:", comboMetodo,
                "Selecione o Cartão (Se aplicável):", comboCartoes,
                "Categoria da Despesa:", comboCat,
                "\nDados de Destino:",
                "CPF do Destinatário:", txtCpf,
                "Número da Conta Destino:", txtConta
        };

        int resultado = JOptionPane.showConfirmDialog(null, componentes, "Nova Transação Financeira", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            return new Object[]{
                    txtValor.getText().trim(),
                    comboMetodo.getSelectedItem(),
                    comboCartoes.getSelectedIndex(),
                    comboCat.getSelectedItem(),
                    txtCpf.getText().trim(),
                    txtConta.getText().trim()
            };
        }
        return null;
    }
}