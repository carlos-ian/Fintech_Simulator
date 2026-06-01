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
    public static String exibirConfirmacaoExclusao() {
        JPasswordField senhaExcluirField = new JPasswordField();
        Object[] formulario = {
                "Para excluir seu perfil, confirme sua senha.\n" +
                        "ATENÇÃO: Esta ação é irreversível e exige saldo zerado em todas as contas.\n\n",
                "Senha Atual:", senhaExcluirField
        };

        int confirmacao = JOptionPane.showConfirmDialog(null, formulario, "Excluir Perfil",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.OK_OPTION) {
            return new String(senhaExcluirField.getPassword());
        }
        return null;
    }
    public static String[] exibirFormularioAlterarDados() {
        String[] camposDisponiveis = {"Nome", "CPF", "Email", "Senha", "Telefone", "DataNascimento"};

        String tipoDadoEscolhido = (String) JOptionPane.showInputDialog(null,
                "Selecione o dado que deseja alterar:",
                "Alterar Dados do Perfil",
                JOptionPane.QUESTION_MESSAGE,
                null,
                camposDisponiveis,
                camposDisponiveis[0]);

        if (tipoDadoEscolhido == null) {
            return null;
        }

        JComponent inputField;
        if ("Senha".equalsIgnoreCase(tipoDadoEscolhido)) {
            inputField = new JPasswordField();
        } else {
            inputField = new JTextField();
        }

        Object[] formularioAlterar = {
                "Digite o novo valor para [" + tipoDadoEscolhido + "]:", inputField
        };

        int confirmacaoAlterar = JOptionPane.showConfirmDialog(null,
                formularioAlterar,
                "Alterar " + tipoDadoEscolhido,
                JOptionPane.OK_CANCEL_OPTION);

        if (confirmacaoAlterar == JOptionPane.OK_OPTION) {
            String novoValor;

            if (inputField instanceof JPasswordField) {
                novoValor = new String(((JPasswordField) inputField).getPassword());
            } else {
                novoValor = ((JTextField) inputField).getText().trim();
            }

            return new String[]{ tipoDadoEscolhido, novoValor };
        }

        return null;
    }
    public static String exibirSeletorStatus(String statusAtual) {
        String[] statusOpcoes = {"ATIVO", "INATIVO"};

        return (String) JOptionPane.showInputDialog(null,
                "O seu perfil está atualmente: " + statusAtual + "\nSelecione o novo status desejado:",
                "Gerenciar Status do Perfil",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOpcoes,
                statusOpcoes[0].equals(statusAtual) ? statusOpcoes[0] : statusOpcoes[1]);
    }
    public static Cartao exibirSeletorCartoes(ArrayList<Cartao> cartoesDaConta) {
        if (cartoesDaConta == null || cartoesDaConta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Esta conta não possui nenhum cartão vinculado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String[] opcoesCartoes = new String[cartoesDaConta.size()];
        for (int i = 0; i < cartoesDaConta.size(); i++) {
            String num = cartoesDaConta.get(i).getNumeroCartao();
            opcoesCartoes[i] = "Cartão **** " + (num.length() >= 4 ? num.substring(num.length() - 4) : num);
        }

        String escolha = (String) JOptionPane.showInputDialog(null, "Selecione o cartão:",
                "Cartões Vinculados", JOptionPane.QUESTION_MESSAGE, null, opcoesCartoes, opcoesCartoes[0]);

        if (escolha == null) return null;

        for (int i = 0; i < opcoesCartoes.length; i++) {
            if (opcoesCartoes[i].equals(escolha)) {
                return cartoesDaConta.get(i);
            }
        }
        return null;
    }
    public static String[] exibirFormularioCriarCartao() {
        JTextField txtNumero = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"DEBITO", "CREDITO", "AMBOS"});
        JTextField txtLimite = new JTextField("0.00");

        Object[] formularioCriar = {
                "Número do Cartão (16 dígitos):", txtNumero,
                "Tipo do Cartão:", comboTipo,
                "Limite de Crédito (Caso aplicável):", txtLimite
        };

        int result = JOptionPane.showConfirmDialog(null, formularioCriar, "Solicitar Novo Cartão", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return new String[]{
                    txtNumero.getText().trim(),
                    (String) comboTipo.getSelectedItem(),
                    txtLimite.getText().trim()
            };
        }
        return null;
    }
    public static String exibirFormularioAjusteLimite() {
        JTextField txtNovoLimite = new JTextField();
        Object[] formularioLimite = {
                "Informe o novo limite total desejado:", txtNovoLimite
        };

        int result = JOptionPane.showConfirmDialog(null, formularioLimite, "Solicitação de Crédito", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return txtNovoLimite.getText().trim();
        }
        return null;
    }
}
