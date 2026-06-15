package Classes.Util;

import Classes.AplicacaoBancaria;
import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Investimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvestimentoBancoRepository {
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

    public static void atualizarValorInvestimento(int investimentoId, double novoValor) {
        String sql = "UPDATE investimento_conta SET valor_aplicado = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, novoValor);
            stmt.setInt(2, investimentoId);
            stmt.executeUpdate();
            System.out.println("DEBUG: Valor do investimento ID " + investimentoId + " atualizado para R$ " + novoValor);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar valor do investimento: " + e.getMessage());
        }
    }
}