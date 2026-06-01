package Classes;

import java.util.ArrayList;


public class Administrador extends Usuario {
    private String matricula;

    public Administrador(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String matricula) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.matricula = matricula;
    }

    public void alterarDados(String tipoDado, String novoValor) {
        if ("Matricula".equalsIgnoreCase(tipoDado)) {
            this.setMatricula(novoValor);
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

    public void setMatricula(String matricula) {this.matricula = matricula;}

    public Cliente consultarCliente(String cpfBusca, ArrayList<Cliente> bancoDeClientes) {
        for (Usuario c : AplicacaoBancaria.ListaUsuarios) {
            if (c.cpf.equals(cpfBusca) && c.tipoUsuario.equals("Cliente")) {
                return (Cliente) c;
            }
        }
        return null;
    }

    public boolean bloquearConta(Conta conta, String justificativa) {
        if (conta.statusConta != Status.BLOQUEADO && justificativa != null) {
            conta.statusConta = Status.BLOQUEADO;
            return true;
        }
        return false;
    }

    public boolean desbloquearConta(Conta conta) {
        if (conta.statusConta == Status.BLOQUEADO) {
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

    public boolean estornarTransacao(Transacao transacao) {
        if (transacao == null || transacao.getStatus().equalsIgnoreCase("ESTORNADO")) {
            return false;
        }

        Conta contaOrigem = transacao.getOrigem();
        Conta contaDestino = transacao.getDestino();
        double valor = transacao.getValor();

        if (contaDestino != null && contaOrigem != null) {
            contaDestino.setSaldo(contaDestino.getSaldo() - valor);
            contaOrigem.setSaldo(contaOrigem.getSaldo() + valor);
        }

        contaDestino.extrato.remove(transacao);
        contaOrigem.extrato.remove(transacao);

        return true;
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
                if (cliente.getStatus() == Status.BLOQUEADO) {
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
    
    public boolean adicionarInvestimento(Investimento i) {
        AplicacaoBancaria.investimentosDisponiveis.add(i);
        return true;
    }

    public boolean removerInvestimento(int id) {
        AplicacaoBancaria.investimentosDisponiveis.remove(id);
        return true;
    }

    public boolean editarInvestimento(Investimento i, String novoNome, double novaTaxa, String novoStatus) {
        if (novoNome == null || novoNome.isBlank()) {return false;}

        if (novaTaxa < 0) {return false;}

        if (novoStatus == null || novoStatus.isBlank()) {return false;}

        i.setNomeProduto(novoNome);
        i.setTaxaRendimento(novaTaxa);
        i.setStatus(novoStatus);
        return true;
    }

    public String getMatricula() {return this.matricula;}
}