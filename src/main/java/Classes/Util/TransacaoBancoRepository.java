package Classes.Util;

import Classes.Model.Conta.Conta;
import Classes.Model.Operacoes.Transacao;
import java.sql.*;
import java.util.ArrayList;

    /**
     * Classe responsável pela persistência e recuperação de transações
     * financeiras no banco de dados.
     *
     * <p>Fornece métodos para registrar transações realizadas entre contas,
     * atualizar os saldos envolvidos e carregar o histórico de movimentações
     * de uma conta para a memória da aplicação.</p>
     *
     * <p>Todas as operações de gravação são executadas dentro de uma transação
     * de banco de dados, garantindo a consistência dos dados em caso de falhas.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class TransacaoBancoRepository {

    /**
     * Registra uma transação financeira no banco de dados e atualiza os
     * saldos das contas envolvidas.
     *
     * <p>O método grava a movimentação da conta de origem, atualiza seu saldo
     * e, quando existir uma conta de destino, atualiza também o saldo da conta
     * destinatária e registra a movimentação de entrada correspondente.</p>
     *
     * <p>Todas as operações são executadas dentro de uma única transação SQL,
     * sendo revertidas automaticamente em caso de erro.</p>
     *
     * @param t                objeto contendo os dados da transação realizada.
     * @param contaOrigemId    identificador da conta de origem.
     * @param novoSaldoOrigem  saldo atualizado da conta de origem após a operação.
     * @param contaDestinoId   identificador da conta de destino. Pode ser
     *                         {@code null} quando não houver destinatário.
     * @param novoSaldoDestino saldo atualizado da conta de destino.
     *                         Pode ser {@code null} quando não houver destinatário.
     * @return
     */

    public static boolean registrarTransacao(Transacao t, int contaOrigemId, double novoSaldoOrigem, Integer contaDestinoId, Double novoSaldoDestino) {
        String sqlTransacao = "INSERT INTO transacao (data_transacao, hora_transacao, valor, categoria, tipo_fluxo, metodo_pagamento, status, conta_origem_id, conta_destino_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAtualizarSaldo = "UPDATE conta SET saldo = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = ConexaoBanco.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtTx = conn.prepareStatement(sqlTransacao)) {
                stmtTx.setString(1, t.getData());
                stmtTx.setString(2, t.getHora());
                stmtTx.setDouble(3, t.getValor());
                stmtTx.setString(4, t.getCategoria());
                stmtTx.setString(5, t.getFluxo());
                stmtTx.setString(6, t.getMetodoPagamento());
                stmtTx.setString(7, t.getStatus());
                stmtTx.setInt(8, contaOrigemId);

                if (contaDestinoId != null) {
                    stmtTx.setInt(9, contaDestinoId);
                } else {
                    stmtTx.setNull(9, Types.INTEGER);
                }
                stmtTx.executeUpdate();
            }

            try (PreparedStatement stmtOrigem = conn.prepareStatement(sqlAtualizarSaldo)) {
                stmtOrigem.setDouble(1, novoSaldoOrigem);
                stmtOrigem.setInt(2, contaOrigemId);
                stmtOrigem.executeUpdate();
            }

            if (contaDestinoId != null && novoSaldoDestino != null) {
                try (PreparedStatement stmtDestino = conn.prepareStatement(sqlAtualizarSaldo)) {
                    stmtDestino.setDouble(1, novoSaldoDestino);
                    stmtDestino.setInt(2, contaDestinoId);
                    stmtDestino.executeUpdate();
                }

                try (PreparedStatement stmtTxDest = conn.prepareStatement(sqlTransacao)) {
                    stmtTxDest.setString(1, t.getData());
                    stmtTxDest.setString(2, t.getHora());
                    stmtTxDest.setDouble(3, t.getValor());
                    stmtTxDest.setString(4, t.getCategoria());
                    stmtTxDest.setString(5, "ENTRADA");
                    stmtTxDest.setString(6, t.getMetodoPagamento());
                    stmtTxDest.setString(7, t.getStatus());
                    stmtTxDest.setInt(8, contaDestinoId);
                    stmtTxDest.setInt(9, contaOrigemId);
                    stmtTxDest.executeUpdate();
                }
            }

            conn.commit();
            return true; // <-- Adicionado: Se tudo deu certo, retorna verdadeiro!

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {} }
            System.err.println("Erro ao registrar transação: " + e.getMessage());
            return false; // <-- Adicionado: Se caiu no catch (deu erro), retorna falso!

        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
        }
        return false;
    }

    /**
     * Carrega do banco de dados todas as transações associadas à conta
     * informada e adiciona os registros à lista de extrato em memória.
     *
     * <p>As transações recuperadas são convertidas para objetos
     * {@link Transacao} e armazenadas na coleção fornecida.</p>
     *
     * @param contaAtual conta cujas transações serão carregadas.
     * @param listaExtratoMemoria lista que receberá as transações recuperadas.
     */

    public static void carregarTransacoes(Conta contaAtual, ArrayList<Transacao> listaExtratoMemoria) {
        String sql = "SELECT * FROM transacao WHERE conta_origem_id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, contaAtual.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Conta contaDestino = null;

                    Transacao t = new Transacao(
                            rs.getString("data_transacao"),
                            rs.getString("hora_transacao"),
                            rs.getDouble("valor"),
                            rs.getString("categoria"),
                            rs.getString("tipo_fluxo"),
                            rs.getString("metodo_pagamento"),
                            rs.getString("status"),
                            contaAtual,
                            contaDestino
                    );

                    listaExtratoMemoria.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar transações: " + e.getMessage());
        }
    }
}