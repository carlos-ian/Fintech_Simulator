package Classes.Model.Usuario;

import Classes.*;
import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Cartao;
import Classes.Model.Operacoes.Justificativa;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

    /**
     * Representa um administrador da fintech.
     *
     * <p>O administrador possui privilégios especiais para gerenciamento
     * do sistema, podendo consultar clientes, ativar e desativar contas,
     * analisar solicitações de aumento de limite, visualizar históricos
     * de transações e gerar relatórios gerenciais.</p>
     *
     * <p>Esta classe herda os atributos e funcionalidades básicas da
     * classe {@link Classes.Model.Usuario.Usuario}, adicionando operações exclusivas de
     * administração.</p>
     *
     * @author Brenno Soares
     * @version 1.0
     * @since 2026
     */

public class Administrador extends Usuario {
    private String matricula;

    /**
     * Cria um novo administrador.
     *
     * @param nome Nome completo do administrador.
     * @param cpf CPF do administrador.
     * @param email E-mail de acesso.
     * @param senha Senha criptografada.
     * @param dataNascimento Data de nascimento.
     * @param telefone Telefone de contato.
     * @param tipoUsuario Tipo de usuário.
     * @param matricula Matrícula funcional do administrador.
     */

    public Administrador(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String matricula) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.matricula = matricula;
    }

    /**
     * Método que implementa o RF08: Alteração de Perfil.
     * <p>Atualiza informações cadastrais do administrador.</p>
     *
     * <p>Permite alterar todos os dados herdados da classe
     * {@link Classes.Model.Usuario.Usuario}, além da matrícula
     * funcional exclusiva da classe Administrador.</p>
     *
     * <p>O status do administrador não pode ser alterado
     * por este método.</p>
     *
     * @param tipoDado Campo que será alterado.
     * @param novoValor Novo valor a ser atribuído.
     *
     * @throws IllegalArgumentException Caso seja realizada
     * uma tentativa de alterar o status do administrador.
     */

    @Override
    public void alterarDados(String tipoDado, String novoValor) {
        if ("Status".equalsIgnoreCase(tipoDado)) {
            throw new IllegalArgumentException("Não é permitido alterar o status do admnistrador por este método.");
        } else if ("Matricula".equalsIgnoreCase(tipoDado)) {
            this.matricula = novoValor;
        } else {
            super.alterarDados(tipoDado, novoValor);
        }
    }

    /**
     * Método que implementa o RF07: Visualização de Perfil.
     * <p>Exibe os dados completos do administrador.</p>
     *
     * <p>Além das informações básicas do perfil, apresenta
     * a matrícula funcional do administrador.</p>
     *
     * @return String formatada contendo os dados do administrador.
     */

    @Override
    public String visualizarDados() {
        return super.visualizarDados() +
                "\nTIPO: Administrador" +
                "\nMATRÍCULA: " + this.matricula;
    }

    /**
     * Método que implementa o RF20: Gerenciamento de Clientes.
     * <p>Procura um cliente cadastrado através do CPF.</p>
     *
     * <p>O Método recebe os valores do cpf do cliente junto a lista
     * de todos os clientes cadastrados na aplicação.</p>
     *
     * @param cpfBusca CPF do cliente a ser localizado.
     * @param bancoDeClientes Lista de clientes cadastrados.
     *
     * @return Cliente encontrado ou {@code null}
     * caso não exista cadastro correspondente.
     */

    public Cliente consultarCliente(String cpfBusca, ArrayList<Cliente> bancoDeClientes) {
        for (Usuario c : AplicacaoBancaria.listaUsuarios) {
            if (c.cpf.equals(cpfBusca) && c.tipoUsuario.equals("Cliente")) {
                return (Cliente) c;
            }
        }
        return null;
    }

    /**
     * Método que implementa o RF21: Gerenciamento de Contas.
     * <p>Desativa uma conta bancária.</p>
     *
     * <p>É informada a conta que deverá ser dessativada e uma justificativa para
     * essa ação. A operação exige obrigatóriamente a indicação de um motivo para
     * ocorrer corretamente a desativação.</p>
     *
     * @param conta Conta que será desativada.
     * @param motivo Motivo da desativação.
     *
     * @return {@code true} caso a conta tenha sido desativada;
     * {@code false} caso já esteja inativa.
     *
     * @throws IllegalArgumentException Caso o motivo seja nulo.
     */

    public boolean desativarConta(Conta conta, Justificativa motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("O motivo da desativação é obrigatório.");
        }

        if (conta.getStatus() != Status.INATIVO) {
            conta.setStatus(Status.INATIVO);
            return true;
        }
        return false;
    }

    /**
     * Método que implementa o RF21: Gerenciamento de Contas.
     * <p>Reativa uma conta previamente desativada.</p>
     *
     * <p>A operação recebe a conta que deverá ser ativada
     * e é executada sem nenhuma condição ou exeção.</p>
     *
     * @param conta Conta a ser ativada.
     *
     * @return {@code true} se a ativação foi realizada;
     * {@code false} caso a conta já esteja ativa.
     */

    public boolean ativarConta(Conta conta) {
        if (conta.getStatus() == Status.INATIVO) {
            conta.setStatus(Status.ATIVO);
            return true;
        }
        return false;
    }

    /**
     * Método que implementa o RF20: Gerenciamento de Clientes.
     * <p>Desativa o perfil de um cliente.</p>
     *
     * <p>É informado o perfil de cliente que deverá ser removido.
     * A desativação somente pode ocorrer mediante justificativa
     * informada pelo administrador.</p>
     *
     * @param cliente Cliente que será desativado.
     * @param motivo Motivo da desativação.
     *
     * @return {@code true} caso a operação seja concluída;
     * {@code false} caso o perfil já esteja desativado.
     *
     * @throws IllegalArgumentException Caso a justificativa seja nula.
     */

    public boolean desativarPerfilCliente(Cliente cliente, Justificativa motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("O motivo da desativação é obrigatório.");
        }

        if (cliente.getStatus() != Status.INATIVO) {
            cliente.setStatus(Status.INATIVO);
            return true;
        }
        return false;
    }

    /**
     * Método que implementa o RF20: Gerenciamento de Clientes.
     * Reativa o perfil de um cliente.
     *
     * <p>A operação recebe o perfil que deverá ser ativado
     * e é executada sem nenhuma condição ou exeção.</p>
     *
     * @param cliente Cliente a ser reativado.
     *
     * @return {@code true} caso a ativação seja realizada;
     * {@code false} caso o perfil já esteja ativo.
     */

    public boolean ativarPerfilCliente(Cliente cliente) {
        if (cliente.getStatus() == Status.ATIVO) {
            return false;
        }
        cliente.setStatus(Status.ATIVO);
        return true;
    }

    /**
     * Método que implementa o RF21: Gerenciamento de Contas.
     * <p>Obtém o histórico completo de transações de uma conta.</p>
     *
     * <p>A operação recebe uma conta, e a partir dela puxa o
     * extrato das transações realizadas por ela.</p>
     *
     * @param conta Conta consultada.
     *
     * @return Lista de transações da conta ou
     * {@code null} caso a conta seja nula.
     */

    public ArrayList<Transacao> visualizarHistoricoConta(Conta conta) {
        if (conta == null) {
            return null;
        }
        return conta.getExtrato();
    }

    /**
     * Método que implementa o RF14: Visualização e Ajuste de Limite.
     * <p>Analisa uma solicitação de aumento de limite de cartão.</p>
     *
     * <p>É passado para a operação, o cartão, a respectiva conta
     * titular do mesmo junto ao novo limite do cartão.
     * O novo limite solicitado é validado considerando:</p>
     *
     * <ul>
     *     <li>Até 150% do limite atual;</li>
     *     <li>No máximo três vezes o saldo da conta.</li>
     * </ul>
     *
     * @param cartao Cartão que receberá o novo limite.
     * @param conta Conta vinculada ao cartão.
     * @param novoLimite Limite solicitado.
     *
     * @return {@code true} caso o pedido seja aprovado;
     * {@code false} caso viole alguma regra de aprovação.
     */

    public boolean analisarPedidoLimite(Cartao cartao, Conta conta, double novoLimite) {
        double limiteAtual = cartao.getLimiteTotal();
        double saldoAtual = conta.getSaldo();

        double crescimentoMaximo = limiteAtual * 1.5;
        double tetoBaseadoNoSaldo = saldoAtual * 3.0;

        if (novoLimite > tetoBaseadoNoSaldo) {
            return false;
        }

        if (novoLimite > crescimentoMaximo) {
            return false;
        }

        return true;
    }

    /**
     * Método que implementa o RF22: Relatório Geral
     * <p>Gera um relatório geral da fintech.</p>
     *
     * <p>O relatório apresenta informações consolidadas sobre:</p>
     *
     * <ul>
     *     <li>Quantidade de clientes;</li>
     *     <li>Clientes ativos e desativados;</li>
     *     <li>Número de contas cadastradas;</li>
     *     <li>Investimentos realizados;</li>
     *     <li>Volume de transações;</li>
     *     <li>Montante total sob custódia.</li>
     * </ul>
     *
     * <p>Além de ser retornado como texto, o relatório
     * também é salvo no arquivo {@code relatorio_fintech.txt}.</p>
     *
     * @param todosUsuarios Lista de clientes cadastrados.
     * @param todasContas Lista de contas existentes.
     * @param todasTransacoes Lista de transações registradas.
     *
     * @return Conteúdo completo do relatório gerado.
     */

    public String gerarRelatorioFintech(ArrayList<Cliente> todosUsuarios, ArrayList<Conta> todasContas, ArrayList<Transacao> todasTransacoes) {
        int totalClientes = todosUsuarios != null ? todosUsuarios.size() : 0;
        int clientesAtivos = 0;
        int clientesDesativados = 0;

        int numeroContas = todasContas != null ? todasContas.size() : 0;
        double montanteTotal = 0;
        int numeroInvestimentosFeitos = 0;

        if (todosUsuarios != null) {
            for (Cliente cliente : todosUsuarios) {
                if (cliente.getStatus() == Status.INATIVO) {
                    clientesDesativados++;
                } else {
                    clientesAtivos++;
                }
            }
        }

        if (todasContas != null) {
            for (Conta conta : todasContas) {
                montanteTotal += conta.getSaldo();

                if (conta.getListaInvestimentos() != null) {
                    numeroInvestimentosFeitos += conta.getListaInvestimentos().size();
                }
            }
        }

        int totalTransacoes = todasTransacoes != null ? todasTransacoes.size() : 0;

        double mediaTransacoesPorConta = numeroContas > 0 ? ((double) totalTransacoes / numeroContas) : 0;

        String conteudoRelatorio = String.format(
                "\n===================================================\n" +
                        "             RELATÓRIO GERAL DA FINTECH            \n" +
                        "===================================================\n" +
                        " GESTÃO DE CLIENTES & CONTAS:\n" +
                        "  -> Total de Clientes Cadastrados: %d\n" +
                        "  -> Clientes Ativos:              %d\n" +
                        "  -> Clientes Desativados:         %d\n" +
                        "  -> Número Total de Contas:       %d\n" +
                        "---------------------------------------------------\n" +
                        " VOLUME DE OPERAÇÕES DO SISTEMA:\n" +
                        "  -> Número de Investimentos Feitos: %d aplicados\n" +
                        "  -> Número de Transações Totais:    %d realizadas\n" +
                        "  -> Média de Transações por Conta:  %.1f operações\n" +
                        "---------------------------------------------------\n" +
                        " FINANCEIRO ANÁLISE:\n" +
                        "  -> Montante Total em Custódia:    R$ %.2f\n" +
                        "===================================================",
                totalClientes, clientesAtivos, clientesDesativados, numeroContas,
                numeroInvestimentosFeitos, totalTransacoes, mediaTransacoesPorConta,
                montanteTotal
        );

        try {
            Path caminho = Paths.get("relatorio_fintech.txt");
            Files.writeString(caminho, conteudoRelatorio);
            System.out.println("Arquivo gravado com sucesso em: " + caminho.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo de relatório: " + e.getMessage());
        }

        return conteudoRelatorio;
    }

    public String getMatricula() {return this.matricula;}
}