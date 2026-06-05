package Classes.Util;

import Classes.Model.Conta.Conta;
import Classes.Model.Conta.ContaCorrente;
import Classes.Model.Conta.ContaInvestimento;
import Classes.Model.Conta.ContaKids;
import Classes.Model.Operacoes.Status;
import Classes.Model.Usuario.Cliente;

import java.sql.*;
import java.util.ArrayList;

public class ContaBancoRepository {

    public static void salvarNoBanco(Conta conta, int usuarioId) {
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

        } catch (SQLException e) {System.err.println("Erro ao salvar conta no banco: " + e.getMessage());}
    }

    public static void carregarContasDoCliente(Cliente cliente) {
        String sql = "SELECT * FROM conta WHERE usuario_id = ?";

        cliente.obterContas().clear();

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
                        contas.get(contas.size() - 1).setStatus(Status.valueOf(rs.getString("status_conta")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar contas do cliente: " + e.getMessage());
        }
    }

    public static void removerDoBanco(String numeroConta) {
        String sql = "DELETE FROM conta WHERE numero_conta = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar conta do banco: " + e.getMessage());
        }
    }
}