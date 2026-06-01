package Classes;

import java.util.ArrayList;


public class Administrador extends Usuario {
    private String matricula;

    public Administrador(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String matricula) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.matricula = matricula;
    }

    public void alterarDados(String tipoDado, String novoValor) {
        if ("Status".equalsIgnoreCase(tipoDado)) {
            throw new IllegalArgumentException("Não é permitido alterar o status do admnistrador por este método.");
        } else if ("Matricula".equalsIgnoreCase(tipoDado)) {
            this.matricula = novoValor;
        } else {
            super.alterarDados(tipoDado, novoValor);
        }
    }

    @Override
    public String visualizarDados() {
        return super.visualizarDados() +
                "\nTIPO: Administrador" +
                "\nMATRÍCULA: " + this.matricula;
    }

    public Cliente consultarCliente(String cpfBusca, ArrayList<Cliente> bancoDeClientes) {
        for (Usuario c : AplicacaoBancaria.ListaUsuarios) {
            if (c.cpf.equals(cpfBusca) && c.tipoUsuario.equals("Cliente")) {
                return (Cliente) c;
            }
        }
        return null;
    }

    public boolean desativarConta(Conta conta, String justificativa) {
        if (conta.statusConta != Status.INATIVO && justificativa != null) {
            conta.statusConta = Status.INATIVO;
            return true;
        }
        return false;
    }

    public boolean ativarConta(Conta conta) {
        if (conta.statusConta == Status.INATIVO) {
            conta.statusConta = Status.ATIVO;
            return true;
        }
        return false;
    }

    public boolean desativarPerfilCliente(Cliente cliente) {
        if (cliente.getStatus() == Status.INATIVO) {
            return false;
        }
        cliente.setStatus(Status.INATIVO);
        return true;
    }

    public boolean ativarPerfilCliente(Cliente cliente) {
        if (cliente.getStatus() == Status.ATIVO) {
            return false;
        }
        cliente.setStatus(Status.ATIVO);
        return true;
    }

    public ArrayList<Transacao> visualizarHistoricoConta(Conta conta) {
        if (conta == null) {
            return null;
        }
        return conta.extrato;
    }

    public boolean analisarPedidoLimite(Cartao cartao, Conta conta, double novoLimite) {
        double limiteAtual = cartao.getLimiteTotal();
        double saldoAtual = conta.saldo; 

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

    public String gerarRelatorioFintech(ArrayList<Cliente> todosUsuarios, ArrayList<Conta> todasContas, ArrayList<Transacao> todasTransacoes) {
        double montanteTotal = 0;
        int usuariosAtivos = 0;
        int usuariosBloqueados = 0;

        double maiorSaldo = 0;
        String contaVIP = "Nenhuma";

        if (todosUsuarios != null) {
            for (Cliente cliente : todosUsuarios) {
                if (cliente.getStatus() == Status.INATIVO) {
                    usuariosBloqueados++;
                } else {
                    usuariosAtivos++;
                }
            }
        }

        if (todasContas != null) {
            for (Conta conta : todasContas) {
                montanteTotal += conta.getSaldo();

                if (conta.getSaldo() > maiorSaldo) {
                    maiorSaldo = conta.getSaldo();
                    contaVIP = conta.getNumeroConta();
                }
            }
        }

        int totalTransacoes = todasTransacoes != null ? todasTransacoes.size() : 0;
        double saldoMedioPorAtivo = usuariosAtivos > 0 ? (montanteTotal / usuariosAtivos) : 0;

        return String.format(
                "\n===================================================\n" +
                        "             RELATÓRIO GERAL DA FINTECH            \n" +
                        "===================================================\n" +
                        " FINANCEIRO:\n" +
                        "  -> MONTANTE TOTAL (SOMA DE SALDOS): R$ %.2f\n" +
                        "  -> Média de Saldo por Usuário Ativo: R$ %.2f\n" +
                        "---------------------------------------------------\n" +
                        " AUDITORIA E DESTAQUES:\n" +
                        "  -> Maior Saldo Individual (VIP):    R$ %.2f (Conta: %s)\n" +
                        "  -> Volume de Operações do Sistema:  %d transações registradas\n" +
                        "---------------------------------------------------\n" +
                        " GESTÃO DE USUÁRIOS:\n" +
                        "  -> NÚMERO TOTAL DE USUÁRIOS ATIVOS: %d\n" +
                        "  -> Número de Usuários Bloqueados:   %d\n" +
                        "===================================================",
                montanteTotal, saldoMedioPorAtivo,
                maiorSaldo, contaVIP, totalTransacoes,
                usuariosAtivos, usuariosBloqueados
        );
    }

    public String getMatricula() {return this.matricula;}
}