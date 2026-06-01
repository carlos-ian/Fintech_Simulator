package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwingUtil {

    // =========================================================================
    // PADRÃO 1: FORMULÁRIO DE DIGITAÇÃO UNIVERSAL (JTextField / JPasswordField)
    // =========================================================================
    /**
     * Cria dinamicamente um formulário vertical com Labels e campos de texto.
     * Mascara o input caso o nome do rótulo contenha a palavra "Senha".
     */
    public static String[] exibirFormulario(String titulo, String mensagem, String... campos) {
        // Cria o painel principal com organização vertical (de cima para baixo)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        // Se houver uma mensagem de instrução, adiciona ela no topo
        if (mensagem != null && !mensagem.isEmpty()) {
            JLabel lblMensagem = new JLabel(mensagem);
            lblMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelPrincipal.add(lblMensagem);
            painelPrincipal.add(Box.createVerticalStrut(10)); // Espaço de 10px abaixo da mensagem
        }

        JTextField[] textFields = new JTextField[campos.length];

        // Loop para criar os rótulos em cima e os inputs embaixo
        for (int i = 0; i < campos.length; i++) {
            JLabel label = new JLabel(campos[i]);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Verifica se o campo é de senha para usar o componente oculto
            if (campos[i].toLowerCase().contains("senha")) {
                textFields[i] = new JPasswordField(20);
            } else {
                textFields[i] = new JTextField(20);
            }
            textFields[i].setAlignmentX(Component.LEFT_ALIGNMENT);

            // Adiciona o rótulo (Texto)
            painelPrincipal.add(label);
            // Adiciona um pequeno espaço de 2px entre o texto e o input
            painelPrincipal.add(Box.createVerticalStrut(2));
            // Adiciona o campo de texto (Input)
            painelPrincipal.add(textFields[i]);
            // Adiciona um espaço de 8px antes de começar o próximo campo
            painelPrincipal.add(Box.createVerticalStrut(8));
        }

        // Exibe a janela modal
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

    // =========================================================================
    // PADRÃO 2: MENU DE SELEÇÃO SUSPENSA (JOptionPane Input Dialog)
    // =========================================================================
    /**
     * Exibe um seletor suspenso (ComboBox) para o usuário escolher um item de uma lista.
     */
    public static String exibirMenuSelecao(String titulo, String mensagem, String... opcoes) {
        if (opcoes == null || opcoes.length == 0) return null;
        return (String) JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    // =========================================================================
    // PADRÃO 3: MENU DE BOTÕES DE AÇÃO INTERATIVA (Option Dialog)
    // =========================================================================
    /**
     * Exibe botões horizontais clicáveis e retorna o índice do botão pressionado.
     */
    public static int exibirMenuBotoes(String titulo, String mensagem, String... botoes) {
        return JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, botoes, botoes[0]);
    }

    // =========================================================================
    // PADRÃO 4: FORMULÁRIO COMPLEXO MISTO (EXCLUSIVO PARA TRANSAÇÕES BANCÁRIAS)
    // =========================================================================
    /**
     * Único método estruturado separadamente por misturar Inputs de texto com ComboBoxes.
     */
    public static Object[] exibirFormularioTransacao(ArrayList<Cartao> cartoes) {
        JTextField txtValor = new JTextField();
        JComboBox<String> comboMetodo = new JComboBox<>(new String[]{"PIX", "DEBITO", "CREDITO"});
        JComboBox<String> comboCat = new JComboBox<>(new String[]{"Alimentação", "Lazer", "Saúde", "Educação", "Transporte", "Outros"});
        JTextField txtCpf = new JTextField();
        JTextField txtConta = new JTextField();

        // Mapeamento dinâmico dos cartões da conta corrente/poupança
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
                "\nDados do Favorecido (Opcional para Transferências):",
                "CPF do Destinatário:", txtCpf,
                "Número da Conta Destino:", txtConta
        };

        int resultado = JOptionPane.showConfirmDialog(null, componentes, "Nova Transação Financeira", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            return new Object[]{
                    txtValor.getText().trim(),
                    comboMetodo.getSelectedItem(),
                    comboCartoes.getSelectedIndex(), // Retorna o index mapeado do cartão
                    comboCat.getSelectedItem(),
                    txtCpf.getText().trim(),
                    txtConta.getText().trim()
            };
        }
        return null;
    }
}