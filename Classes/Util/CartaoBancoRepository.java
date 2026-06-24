package Classes.Util;

import Classes.Model.Operacoes.Cartao;
import java.sql.*;
import java.util.ArrayList;

    /**
     * Classe responsável pela persistência dos cartões no banco de dados.
     *
     * <p>Implementa operações de acesso aos dados (DAO/Repository)
     * relacionadas aos cartões cadastrados no sistema, permitindo
     * salvar, atualizar e carregar informações armazenadas na base
     * de dados.</p>
     *
     * <p>Todos os métodos são estáticos, pois a classe atua apenas
     * como intermediária entre a aplicação e a tabela de cartões
     * do banco de dados.</p>
     *
     * @author Ian Carlos
     * @version 1.0
     * @since 2026
     */

public class CartaoBancoRepository {

    /**
     * Salva um novo cartão no banco de dados.
     *
     * <p>Os dados do cartão são inseridos na tabela
     * <code>cartao</code> e associados à conta informada
     * por meio da chave estrangeira <code>conta_id</code>.</p>
     *
     * @param cartao Cartão que será persistido.
     * @param contaId Identificador da conta à qual o cartão pertence.
     */

    public static void salvarCartao(Cartao cartao, int contaId) {
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
        } catch (SQLException e) {
            System.err.println("Erro ao salvar cartão no banco: " + e.getMessage());
        }
    }

        /**
         * Atualiza o status de bloqueio de um cartão no banco.
         *
         * <p>Esse método é utilizado quando um cartão é bloqueado
         * ou desbloqueado dentro do sistema.</p>
         *
         * @param numeroCartao Número do cartão a ser atualizado.
         * @param estaBloqueado Novo status do cartão.
         * <ul>
         *     <li>{@code true} - Cartão bloqueado</li>
         *     <li>{@code false} - Cartão ativo</li>
         * </ul>
         */

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

        /**
         * Atualiza os limites de crédito de um cartão.
         *
         * <p>O método altera tanto o limite total quanto
         * o limite disponível armazenados no banco de dados.</p>
         *
         * @param numeroCartao Número do cartão a ser atualizado.
         * @param limiteTotal Novo limite total do cartão.
         * @param limiteDisponivel Novo limite disponível para uso.
         */

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

        /**
         * Carrega todos os cartões vinculados a uma conta.
         *
         * <p>Os cartões encontrados são convertidos em objetos
         * da classe {@link Cartao} e adicionados à lista
         * mantida em memória pela aplicação.</p>
         *
         * <p>Caso o cartão esteja marcado como bloqueado
         * no banco de dados, o seu estado também será atualizado
         * no objeto carregado.</p>
         *
         * @param contaId Identificador da conta proprietária
         *                dos cartões.
         * @param listaCartoesMemoria Lista que receberá os cartões
         *                            carregados do banco.
         */

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