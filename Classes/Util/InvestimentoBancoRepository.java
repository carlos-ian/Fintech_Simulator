package Classes.Util;

import Classes.AplicacaoBancaria;
import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Investimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    /**
     * Classe responsável pela persistência e recuperação dos investimentos
     * realizados pelos clientes da fintech.
     *
     * <p>Esta classe implementa o padrão Repository e centraliza todas as
     * operações relacionadas aos investimentos armazenados no banco de dados,
     * incluindo:</p>
     *
     * <ul>
     *     <li>Registro de novas aplicações financeiras;</li>
     *     <li>Processamento de resgates de investimentos;</li>
     *     <li>Carregamento dos investimentos vinculados a uma conta.</li>
     * </ul>
     *
     * <p>Os investimentos recuperados do banco são convertidos para objetos
     * da classe {@link Investimento} e adicionados à lista de investimentos
     * da conta correspondente.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class InvestimentoBancoRepository {

    /**
     * Registra uma nova aplicação financeira no banco de dados.
     *
     * <p>O método realiza duas operações dentro de uma transação:</p>
     * <ul>
     *     <li>Insere o investimento na tabela de aplicações;</li>
     *     <li>Atualiza o saldo da conta após o investimento.</li>
     * </ul>
     *
     * <p>Caso ambas as operações sejam concluídas com sucesso,
     * as alterações são confirmadas no banco.</p>
     *
     * @param conta Conta responsável pela aplicação.
     * @param produto Produto financeiro selecionado.
     * @param valor Valor aplicado no investimento.
     * @param data Data da aplicação.
     */

    public static void registrarInvestimento(Conta conta, Investimento produto, double valor, String data) {
        String sqlAplicacao = "INSERT INTO investimento_conta (conta_id, nome_produto, valor_aplicado, data_aplicacao) VALUES (?, ?, ?, ?)";
        String sqlAtualizarSaldo = "UPDATE conta SET saldo = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtA = conn.prepareStatement(sqlAplicacao)) {
                stmtA.setInt(1, conta.getId());
                stmtA.setString(2, produto.getNomeProduto());
                stmtA.setDouble(3, valor);
                stmtA.setString(4, data);
                stmtA.executeUpdate();
            }

            try (PreparedStatement stmtS = conn.prepareStatement(sqlAtualizarSaldo)) {
                stmtS.setDouble(1, conta.getSaldo());
                stmtS.setInt(2, conta.getId());
                stmtS.executeUpdate();
            }

            conn.commit();
            System.out.println("DEBUG: Aplicação de " + produto.getNomeProduto() + " salva com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao registrar aplicação no banco: " + e.getMessage());
        }
    }

    /**
     * Processa o resgate de um investimento registrado na conta.
     *
     * <p>O método remove o investimento da tabela de aplicações
     * e atualiza o saldo da conta com o valor resgatado.</p>
     *
     * <p>As operações são executadas dentro de uma transação para
     * garantir a integridade dos dados.</p>
     *
     * @param conta Conta que receberá o valor resgatado.
     * @param nomeProduto Nome do produto financeiro a ser resgatado.
     * @param valorAntigo Valor originalmente aplicado no investimento.
     */

    public static void resgatarInvestimento(Conta conta, String nomeProduto, double valorAntigo) {
        String sqlDeletar = "DELETE FROM investimento_conta WHERE id = " +
                "(SELECT id FROM investimento_conta WHERE conta_id = ? AND nome_produto = ? AND valor_aplicado = ? LIMIT 1)";
        String sqlAtualizarSaldo = "UPDATE conta SET saldo = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtD = conn.prepareStatement(sqlDeletar)) {
                stmtD.setInt(1, conta.getId());
                stmtD.setString(2, nomeProduto);
                stmtD.setDouble(3, valorAntigo);
                stmtD.executeUpdate();
            }

            try (PreparedStatement stmtS = conn.prepareStatement(sqlAtualizarSaldo)) {
                stmtS.setDouble(1, conta.getSaldo());
                stmtS.setInt(2, conta.getId());
                stmtS.executeUpdate();
            }

            conn.commit();
            System.out.println("DEBUG: Resgate de " + nomeProduto + " processado no banco!");
        } catch (SQLException e) {
            System.err.println("Erro ao processar resgate no banco: " + e.getMessage());
        }
    }

    /**
     * Carrega todos os investimentos associados a uma conta.
     *
     * <p>Para cada registro encontrado no banco de dados, é criado
     * um objeto {@link Investimento} contendo:</p>
     *
     * <ul>
     *     <li>Identificador do investimento;</li>
     *     <li>Nome do produto financeiro;</li>
     *     <li>Valor aplicado;</li>
     *     <li>Data da aplicação;</li>
     *     <li>Taxa de rendimento correspondente ao produto.</li>
     * </ul>
     *
     * <p>Antes do carregamento, a lista atual de investimentos
     * da conta é limpa para evitar duplicidades.</p>
     *
     * @param conta Conta cujos investimentos serão carregados.
     */

    public static void carregarInvestimentos(Conta conta) {
        conta.getListaInvestimentos().clear();
        String sql = "SELECT id, nome_produto, valor_aplicado, data_aplicacao FROM investimento_conta WHERE conta_id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, conta.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idBanco = rs.getInt("id");
                    String nome = rs.getString("nome_produto");
                    double valor = rs.getDouble("valor_aplicado");
                    String data = rs.getString("data_aplicacao");

                    double taxaRendimento = 0.0;
                    for (Investimento fixo : AplicacaoBancaria.investimentosDisponiveis) {
                        if (fixo.getNomeProduto().equalsIgnoreCase(nome)) {
                            taxaRendimento = fixo.getTaxaRendimento();
                            break;
                        }
                    }

                    Investimento inv = new Investimento(nome, taxaRendimento, valor, data);
                    inv.setId(idBanco);

                    conta.getListaInvestimentos().add(inv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar investimentos da conta: " + e.getMessage());
        }
    }
}