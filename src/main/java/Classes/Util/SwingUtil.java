package Classes.Util;

import Classes.Model.Operacoes.Cartao;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

    /**
     * Classe utilitária responsável pela criação e exibição de componentes
     * gráficos da interface utilizando Swing.
     *
     * <p>Esta classe centraliza a exibição de formulários, menus e caixas
     * de diálogo utilizados pela aplicação, facilitando a interação do
     * usuário com o sistema bancário.</p>
     *
     * <p>Todos os métodos são estáticos, não sendo necessária a criação
     * de objetos desta classe.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class SwingUtil {

    /**
     * Exibe um formulário dinâmico contendo os campos informados.
     *
     * <p>Os campos são criados automaticamente a partir dos nomes
     * recebidos como parâmetro. Caso o nome de um campo contenha
     * a palavra "senha", será utilizado um {@link JPasswordField}
     * para ocultar os caracteres digitados.</p>
     *
     * <p>Ao confirmar o formulário, os valores preenchidos são
     * retornados em um vetor de Strings na mesma ordem dos campos.
     * Caso o usuário cancele a operação, o método retorna {@code null}.</p>
     *
     * @param titulo Título da janela exibida.
     * @param mensagem Mensagem informativa apresentada ao usuário.
     * @param campos Lista de campos que serão exibidos no formulário.
     * @return Vetor contendo os dados informados ou {@code null}
     * caso a operação seja cancelada.
     */

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

    /**
     * Exibe um menu de seleção baseado numa lista de opções.
     *
     * <p>O usuário poderá escolher apenas uma das opções disponíveis
     * por meio de uma caixa de seleção.</p>
     *
     * @param titulo Título da janela.
     * @param mensagem Mensagem exibida ao usuário.
     * @param opcoes Opções disponíveis para seleção.
     * @return Opção selecionada pelo usuário ou {@code null}
     * caso a janela seja cancelada ou não existam opções.
     */

    public static String exibirMenuSelecao(String titulo, String mensagem, String... opcoes) {
        if (opcoes == null || opcoes.length == 0) return null;
        return (String) JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    /**
     * Exibe uma janela contendo botões personalizados.
     *
     * <p>O índice do botão selecionado é retornado para que a aplicação
     * possa identificar qual ação o usuário escolheu.</p>
     *
     * @param titulo Título da janela.
     * @param mensagem Mensagem apresentada ao usuário.
     * @param botoes Botões que serão exibidos.
     * @return Índice do botão selecionado ou valor negativo
     * caso a janela seja fechada.
     */

    public static int exibirMenuBotoes(String titulo, String mensagem, String... botoes) {
        return JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, botoes, botoes[0]);
    }

        /**
         * Exibe um formulário completo para cadastro de uma nova transação
         * financeira.
         *
         * <p>O formulário permite informar:</p>
         *
         * <ul>
         *     <li>Valor da operação;</li>
         *     <li>Método de pagamento;</li>
         *     <li>Cartão utilizado;</li>
         *     <li>Categoria da despesa;</li>
         *     <li>CPF do destinatário;</li>
         *     <li>Número da conta de destino.</li>
         * </ul>
         *
         * <p>Os cartões recebidos são exibidos em formato mascarado,
         * mostrando apenas os quatro últimos dígitos.</p>
         *
         * <p>Ao confirmar o formulário, os dados preenchidos são retornados
         * em um vetor de objetos. Caso a operação seja cancelada,
         * o método retorna {@code null}.</p>
         *
         * @param cartoes Lista de cartões disponíveis para utilização
         * durante a transação.
         * @return Vetor contendo os dados da transação ou {@code null}
         * caso o usuário cancele a operação.
         */

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