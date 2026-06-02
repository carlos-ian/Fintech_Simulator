package Classes;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public boolean ativarConta(Conta conta) {
        if (conta.statusConta == Status.INATIVO) {
            conta.statusConta = Status.ATIVO;
            return true;
        }
        return false;
    }

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

                if (conta.listaInvestimentos != null) {
                    numeroInvestimentosFeitos += conta.listaInvestimentos.size();
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