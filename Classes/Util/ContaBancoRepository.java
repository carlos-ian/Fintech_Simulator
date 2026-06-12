package Classes.Util;

import Classes.Model.Conta.Conta;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Conta.ContaInvestimento;
import Classes.Model.Conta.ContaKids;
import Classes.Model.Usuario.Cliente;
import Classes.Model.Operacoes.Status;

import java.sql.*;
import java.util.ArrayList;

public class ContaBancoRepository {

    public static void salvarConta(Conta conta, int usuarioId) {
        String sql = "INSERT INTO conta (numero_conta, agencia, saldo, tipo_conta, status_conta, " +
                "limite_cheque_especial, cpf_responsavel, limite_mensal, titular_cpf, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getNumeroConta());
            stmt.setString(2, conta.getAgencia());
            stmt.setDouble(3, conta.getSaldo());
            stmt.setString(4, conta.getTipoConta());
            stmt.setString(5, conta.getStatus().name());

            stmt.setNull(6, Types.NUMERIC);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.NUMERIC);
            stmt.setNull(9, Types.VARCHAR);

            if (conta instanceof ContaCorrente) {
                stmt.setDouble(6, ((ContaCorrente) conta).getLimiteChequeEspecial());
            } else if (conta instanceof ContaKids) {
                stmt.setString(7, ((ContaKids) conta).getCpfResponsavel());
                stmt.setDouble(8, ((ContaKids) conta).getLimiteMensal());
            } else if (conta instanceof ContaInvestimento) {
                stmt.setString(9, ((ContaInvestimento) conta).getTitularCPF());
            }

            stmt.setInt(10, usuarioId);
            stmt.executeUpdate();

        } catch (SQLException e) { System.err.println("Erro ao salvar conta no banco: " + e.getMessage()); }
    }

    public static void carregarContas(Cliente cliente) {
        String sql = "SELECT * FROM conta WHERE usuario_id = ?";
        cliente.obterContas().clear();

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idContaBanco = rs.getInt("id");
                    String tipo = rs.getString("tipo_conta");
                    String num = rs.getString("numero_conta");
                    String ag = rs.getString("agencia");
                    double sal = rs.getDouble("saldo");

                    if (tipo.equalsIgnoreCase("Conta Corrente")) {
                        cliente.abrirConta(num, ag, sal, tipo, rs.getDouble("limite_cheque_especial"), null, 0, null);
                    } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                        cliente.abrirConta(num, ag, sal, tipo, 0, null, 0, null);
                    } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                        cliente.abrirConta(num, ag, sal, tipo, 0, rs.getString("cpf_responsavel"), rs.getDouble("limite_mensal"), null);
                    } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                        cliente.abrirConta(num, ag, sal, tipo, 0, null, 0, rs.getString("titular_cpf"));
                    }

                    ArrayList<Conta> contas = cliente.obterContas();
                    if (!contas.isEmpty()) {
                        Conta contaRecemCarregada = contas.get(contas.size() - 1);
                        contaRecemCarregada.setId(idContaBanco);
                        contaRecemCarregada.setStatus(Status.valueOf(rs.getString("status_conta")));

                        CartaoBancoRepository.carregarCartoes(idContaBanco, contaRecemCarregada.getCartoes());
                        InvestimentoBancoRepository.carregarInvestimentos(contaRecemCarregada);
                        TransacaoBancoRepository.carregarTransacoes(contaRecemCarregada, contaRecemCarregada.getExtrato());
                    }
                }
            }
        } catch (SQLException e) { System.err.println("Erro ao carregar contas do cliente: " + e.getMessage()); }
    }

    public static void deletarConta(String numeroConta) {
        String sql = "DELETE FROM conta WHERE numero_conta = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro ao deletar conta do banco: " + e.getMessage()); }
    }

    public static Conta buscarContaDestino(String cpfDestino, String numeroContaDestino) {
        String sql = "SELECT c.* FROM conta c " +
                "JOIN usuario u ON c.usuario_id = u.id " +
                "WHERE u.cpf = ? AND c.numero_conta = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfDestino);
            stmt.setString(2, numeroContaDestino);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo_conta");
                    String num = rs.getString("numero_conta");
                    String ag = rs.getString("agencia");
                    double sal = rs.getDouble("saldo");
                    int idConta = rs.getInt("id");

                    Conta contaDest = null;

                    if (tipo.equalsIgnoreCase("Conta Corrente")) {
                        contaDest = new Classes.Model.Conta.ContaCorrente(num, ag, sal, "Conta Corrente", rs.getDouble("limite_cheque_especial"));
                    } else if (tipo.equalsIgnoreCase("Conta Poupança")) {
                        contaDest = new Classes.Model.Conta.ContaPoupanca(num, ag, sal, "Conta Poupança");
                    } else if (tipo.equalsIgnoreCase("Conta Kids")) {
                        contaDest = new Classes.Model.Conta.ContaKids(num, ag, sal, "Conta Kids", rs.getString("cpf_responsavel"), rs.getDouble("limite_mensal"));
                    } else if (tipo.equalsIgnoreCase("Conta Investimento")) {
                        contaDest = new Classes.Model.Conta.ContaInvestimento(num, ag, sal, "Conta Investimento", rs.getString("titular_cpf"));
                    }

                    if (contaDest != null) {
                        contaDest.setId(idConta);
                        String statusBanco = rs.getString("status_conta").trim().toUpperCase();
                        contaDest.setStatus(Classes.Model.Operacoes.Status.valueOf(statusBanco));
                    }

                    return contaDest;
                }
            }
        } catch (SQLException e) { System.err.println("Erro ao buscar conta de destino: " + e.getMessage()); }
        return null;
    }

    public static void atualizarStatusConta(int idConta, Classes.Model.Operacoes.Status novoStatus) {
        String sql = "UPDATE conta SET status_conta = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, idConta);
            stmt.executeUpdate();
            System.out.println("Status da conta ID " + idConta + " atualizado para " + novoStatus.name() + " no banco.");

        } catch (SQLException e) { System.err.println("Erro ao atualizar status da conta no banco: " + e.getMessage()); }
    }
}