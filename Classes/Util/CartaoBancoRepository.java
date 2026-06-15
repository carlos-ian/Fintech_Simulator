package Classes.Util;

import Classes.Model.Operacoes.Cartao;
import java.sql.*;
import java.util.ArrayList;

public class CartaoBancoRepository {

    public static boolean salvarCartao(Cartao cartao, int contaId) {
        String sql = "INSERT INTO cartao (numero_cartao, titular, tipo_cartao, limite_total, limite_disponivel, esta_bloqueado, conta_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cartao.getNumeroCartao());
            stmt.setString(2, cartao.getTitular());
            stmt.setString(3, cartao.getTipoCartao());
            stmt.setDouble(4, cartao.getLimiteTotal());
            stmt.setDouble(5, cartao.getLimiteDisponivel());
            stmt.setBoolean(6, cartao.getEstaBloqueado());
            stmt.setInt(7, contaId);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar cartão no banco: " + e.getMessage());
            return false;
        }
    }

    public static void atualizarBloqueioCartao(String numeroCartao, boolean estaBloqueado) {
        String sql = "UPDATE cartao SET esta_bloqueado = ? WHERE numero_cartao = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, estaBloqueado);
            stmt.setString(2, numeroCartao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do cartão: " + e.getMessage());
        }
    }

    public static void atualizarLimitesCartao(String numeroCartao, double limiteTotal, double limiteDisponivel) {
        String sql = "UPDATE cartao SET limite_total = ?, limite_disponivel = ? WHERE numero_cartao = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, limiteTotal);
            stmt.setDouble(2, limiteDisponivel);
            stmt.setString(3, numeroCartao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar limites do cartão: " + e.getMessage());
        }
    }

    public static void carregarCartoes(int contaId, ArrayList<Cartao> listaCartoesMemoria) {
        String sql = "SELECT * FROM cartao WHERE conta_id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, contaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cartao c = new Cartao(
                            rs.getString("numero_cartao"),
                            rs.getString("titular"),
                            rs.getString("tipo_cartao"),
                            rs.getDouble("limite_total"),
                            rs.getDouble("limite_disponivel")
                    );
                    if (rs.getBoolean("esta_bloqueado")) {
                        c.bloquearCartao();
                    }
                    listaCartoesMemoria.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar cartões da conta: " + e.getMessage());
        }
    }
}