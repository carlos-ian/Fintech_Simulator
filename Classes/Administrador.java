package Classes;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import Classes.Exceptions.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Administrador extends Usuario {

    private String matricula;

    // O construtor deve seguir a mesma assinatura da superclasse Usuario.java
    public Administrador(String nome, String cpf, String email, String senha, String dataNascimento, String telefone, String tipoUsuario, String matricula) {
        super(nome, cpf, email, senha, dataNascimento, telefone, tipoUsuario);
        this.matricula = matricula;
    }

    // ========================================================================
    // RF16 - Gerenciamento de Contas e Perfis
    // ========================================================================
    
    // Busca o cliente numa lista simulada de banco de dados
    public Cliente consultarCliente(String cpfBusca, ArrayList<Cliente> bancoDeClientes) {
        for (Cliente c : bancoDeClientes) {
            // Como 'cpf' é protected em Usuario.java, podemos acessá-lo diretamente
            if (c.cpf.equals(cpfBusca)) {
                System.out.println("Cliente encontrado: " + c.nome);
                return c;
            }
        }
        System.out.println("Cliente com CPF " + cpfBusca + " não encontrado.");
        return null;
    }

    // Bloqueia a conta alterando o Enum Status (Status.java)
    public void bloquearConta(Conta conta, String justificativa) {
        if (conta.statusConta != Status.BLOQUEADO) {
            conta.statusConta = Status.BLOQUEADO;
            System.out.println("Conta " + conta.numeroConta + " BLOQUEADA. Motivo: " + justificativa);
        } else {
            System.out.println("A conta já encontra-se bloqueada.");
        }
    }

    public void desbloquearConta(Conta conta) {
        if (conta.statusConta == Status.BLOQUEADO) {
            conta.statusConta = Status.ATIVO;
            System.out.println("Conta " + conta.numeroConta + " DESBLOQUEADA com sucesso.");
        }
    }

    // ========================================================================
    // RF17 - Análise e Aprovação de Crédito
    // ========================================================================
    
    // Este método foi chamado na linha 79 de Cartao.java que o seu colega fez: 
    // boolean resultadoAnalise = admin.analisarPedidoLimite(this, novoLimiteSolicitado);
    public boolean analisarPedidoLimite(Cartao cartao, Conta conta, double novoLimite) {
        // É necessário que seu colega crie o método getLimiteTotal() na classe Cartao
        double limiteAtual = cartao.getLimiteTotal(); 
        
        // Acessamos o saldo diretamente pois o atributo é protected na superclasse Conta
        double saldoAtual = conta.saldo; 
        
        // --- Definição das Regras de Negócio ---
        double crescimentoMaximo = limiteAtual * 1.5; // Regra 1: Aumento máximo de 50% por vez
        double tetoBaseadoNoSaldo = saldoAtual * 3.0; // Regra 2: Limite máximo de 3x o saldo do cliente

        System.out.println("\n[SISTEMA ADMIN] Analisando solicitação de limite para o cartão final " + cartao.getTipoCartao() + "...");

        // 1. Validação de Lastro Financeiro (Regra 2)
        if (novoLimite > tetoBaseadoNoSaldo) {
            System.out.printf("[SISTEMA ADMIN] Negado: O limite solicitado (R$ %.2f) excede a margem de segurança baseada no saldo atual (Max: R$ %.2f).\n", novoLimite, tetoBaseadoNoSaldo);
            return false;
        }

        // 2. Validação de Crescimento Gradual (Regra 1)
        if (novoLimite > crescimentoMaximo) {
            System.out.printf("[SISTEMA ADMIN] Negado: Aumento muito brusco. O limite só pode ser elevado em até 50%% por vez (Max: R$ %.2f).\n", crescimentoMaximo);
            return false;
        }

        // Se passou pelas duas travas, está aprovado
        System.out.println("[SISTEMA ADMIN] Crédito Aprovado com sucesso sob as diretrizes de risco da fintech.");
        return true;
    }

    // ========================================================================
    // RF18 - Visualização de Transações
    // ========================================================================
    
    public void visualizarTransacoes(Conta conta) {
        System.out.println("\n[SISTEMA ADMIN] Puxando histórico completo da conta: " + conta.numeroConta);
        // Aproveitamos o método que já existe em Conta.java
        // Passando null para os filtros, ele trará o extrato completo
        conta.visualizarExtrato(null, null, null, null, null);
    }

    // ========================================================================
    // RF19 - Estorno de Transações
    // ========================================================================
    
    public boolean estornarTransacao(Transacao t, Conta contaDestino) {
        System.out.println("\n[SISTEMA ADMIN] Iniciando processo de estorno para transação ID: " + t.getId());
        
        // Simulação de estorno: em um caso real, você adicionaria o saldo de volta.
        // Como o atributo 'saldo' é protected em Conta.java, você pode manipulá-lo.
        System.out.println("Estorno da transação de " + t.getCategoria() + " realizado com sucesso.");
        t.setTipoFluxo("ESTORNADO");
        return true;
    }

    // ========================================================================
    // RF20 - Geração de Relatório Geral
    // ========================================================================
    
    public void gerarRelatorioGeral(ArrayList<Conta> todasAsContas, ArrayList<Usuario> todosOsUsuarios) {
        double montanteTotal = 0.0;
        int clientesAtivos = 0;

        // Soma o saldo de todas as contas
        for (Conta c : todasAsContas) {
            montanteTotal += c.saldo; 
        }

        // Conta quantos usuários na lista são instâncias da classe Cliente
        for (Usuario u : todosOsUsuarios) {
            if (u instanceof Cliente) {
                clientesAtivos++;
            }
        }

        System.out.println("\n================ RELATÓRIO GERAL FINTECH ================");
        System.out.println("Total de Usuários Cadastrados (Geral): " + todosOsUsuarios.size());
        System.out.println("Total de Clientes Ativos: " + clientesAtivos);
        System.out.printf("Montante Total Gerido pela Plataforma: R$ %.2f\n", montanteTotal);
        System.out.println("=========================================================\n");
    }

    // ========================================================================
    // RF21 - Manutenção de Tipos de Investimento
    // ========================================================================
    
    public void adicionarInvestimento(Investimento i, ArrayList<Investimento> vitrine) {
        vitrine.add(i);
        // Assumindo que a classe Investimento terá o método getNomeProduto()
        System.out.println("[SISTEMA ADMIN] Novo investimento '" + i.getNomeProduto() + "' adicionado à vitrine com sucesso.");
    }

    public boolean removerInvestimento(int id, ArrayList<Investimento> vitrine) {
        for (int j = 0; j < vitrine.size(); j++) {
            if (vitrine.get(j).getIdInvestimento() == id) {
                vitrine.remove(j);
                System.out.println("[SISTEMA ADMIN] Investimento ID " + id + " removido da vitrine.");
                return true;
            }
        }
        System.out.println("[SISTEMA ADMIN] Erro: Investimento com ID " + id + " não encontrado.");
        return false;
    }

    public void editarInvestimento(Investimento investimentoEditado, ArrayList<Investimento> vitrine) {
        for (int j = 0; j < vitrine.size(); j++) {
            // Procura o investimento na vitrine que tenha o mesmo ID do investimento editado
            if (vitrine.get(j).getIdInvestimento() == investimentoEditado.getIdInvestimento()) {
                vitrine.set(j, investimentoEditado); // Substitui o objeto antigo pelo novo
                System.out.println("[SISTEMA ADMIN] Investimento ID " + investimentoEditado.getIdInvestimento() + " atualizado na vitrine.");
                return;
            }
        }
        System.out.println("[SISTEMA ADMIN] Falha na edição: Investimento não encontrado na vitrine.");
    }

    public String getMatricula() {
        return matricula;
    }

    // ========================================================================
    // CRITÉRIO H - Persistência de Dados em Arquivos (.CSV)
    // ========================================================================

    /**
     * Salva a lista atual de investimentos no arquivo CSV.
     */
    public void salvarVitrineCSV(ArrayList<Investimento> vitrine) {
        String caminhoArquivo = "vitrine_investimentos.csv";

        // O try-with-resources já fecha o arquivo automaticamente
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            // Escreve o cabeçalho (opcional, mas boa prática no CSV)
            bw.write("ID;Nome;Taxa;ValorAplicado;Data;Status");
            bw.newLine();

            // Escreve cada objeto da lista em uma nova linha
            for (Investimento inv : vitrine) {
                String linha = inv.getIdInvestimento() + ";" +
                               inv.getNomeProduto() + ";" +
                               inv.getTaxaRendimento() + ";" +
                               inv.getValorAplicado() + ";" +
                               inv.getDataAplicacao() + ";" +
                               inv.getStatus();
                bw.write(linha);
                bw.newLine();
            }
            System.out.println("[SISTEMA ADMIN] Vitrine salva com sucesso no arquivo " + caminhoArquivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar arquivo de investimentos: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega os dados do arquivo CSV e preenche a lista do sistema ao iniciar.
     */
    public void carregarVitrineCSV(ArrayList<Investimento> vitrine) {
        String caminhoArquivo = "vitrine_investimentos.csv";
        vitrine.clear(); // Limpa a lista atual para evitar duplicação

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha = br.readLine(); // Lê a primeira linha (cabeçalho)
            
            // Se o arquivo não estiver vazio, lê o resto
            if (linha != null) {
                linha = br.readLine(); // Pula para a primeira linha de dados
                while (linha != null) {
                    String[] dados = linha.split(";"); // Separa os atributos pelo delimitador
                    
                    if (dados.length == 6) { // Garante que a linha tem todos os dados
                        // Pula o ID (dados[0]) porque o construtor do seu colega gera automático
                        String nome = dados[1];
                        double taxa = Double.parseDouble(dados[2]);
                        double valor = Double.parseDouble(dados[3]);
                        String data = dados[4];
                        String status = dados[5];

                        // Cria o objeto e adiciona à vitrine virtual
                        Investimento invCarregado = new Investimento(nome, taxa, valor, data, status);
                        vitrine.add(invCarregado);
                    }
                    linha = br.readLine(); // Avança para a próxima linha
                }
            }
            System.out.println("[SISTEMA ADMIN] Vitrine carregada do arquivo " + caminhoArquivo + " com sucesso.");
        } catch (IOException e) {
            System.out.println("[SISTEMA ADMIN] Nenhum arquivo anterior encontrado ou erro de leitura. Iniciando com vitrine vazia.");
        } catch (NumberFormatException e) {
            System.out.println("[SISTEMA ADMIN] Erro de formatação nos dados do arquivo CSV.");
        }
    }

    public void cobrarTaxaAdministrativa(Conta conta, double valorTaxa) {
        // Iniciamos o bloco 'try' para tentar executar a ação de risco
        try {
            // Simulamos a cobrança usando o método já existente.
            // Passamos "PIX" como simulação de débito direto em conta e null para cartão e destino.
            boolean sucesso = conta.realizarTransacao(valorTaxa, "PIX", null, "Taxa Administrativa", null);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, 
                    "Taxa administrativa de R$ " + valorTaxa + " cobrada com sucesso da conta " + conta.numeroConta, 
                    "Operação Concluída", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ContaInativaException e) {
            // Tratamento específico caso a conta do cliente não esteja ATIVA
            JOptionPane.showMessageDialog(null, 
                "Operação bloqueada: " + e.getMessage(), 
                "Status de Conta Inválido", 
                JOptionPane.ERROR_MESSAGE);

        } catch (SaldoInsuficienteException e) {
            // Tratamento caso o cliente não tenha dinheiro para pagar a taxa
            JOptionPane.showMessageDialog(null, 
                "Falha na cobrança: O cliente não possui saldo para cobrir a taxa administrativa.\nDetalhe: " + e.getMessage(), 
                "Saldo Insuficiente", 
                JOptionPane.WARNING_MESSAGE);

        } catch (LimiteInsuficienteException e) {
            // O Java exige o catch desta exceção pois a assinatura do realizarTransacao a declara,
            // mesmo que no "PIX" ela não seja engatilhada diretamente.
            JOptionPane.showMessageDialog(null, 
                "Erro de Limite: " + e.getMessage(), 
                "Limite Insuficiente", 
                JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException e) {
            // Captura erros de parâmetros inválidos (ex: método de pagamento inexistente)
            JOptionPane.showMessageDialog(null, 
                "Erro de validação do sistema: " + e.getMessage(), 
                "Erro de Parâmetro", 
                JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            // Um "catch-all" genérico para garantir que o sistema não quebre por erros não mapeados
            JOptionPane.showMessageDialog(null, 
                "Ocorreu um erro inesperado ao processar a taxa: " + e.getMessage(), 
                "Erro Crítico", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Exibe o ecrã/menu principal do Administrador com opções gráficas.
     
    public void exibirMenuAdministrativo(ArrayList<Cliente> bancoDeClientes, ArrayList<Conta> todasAsContas, ArrayList<Usuario> todosOsUsuarios, ArrayList<Investimento> vitrine) {
        boolean continuar = true;

        while (continuar) {
            String menu = "=== PAINEL DE ADMINISTRAÇÃO ===\n" +
                          "Matrícula: " + this.matricula + "\n\n" +
                          "1. Consultar Cliente (RF16)\n" +
                          "2. Bloquear Conta (RF16)\n" +
                          "3. Desbloquear Conta (RF16)\n" +
                          "4. Visualizar Histórico de Transações (RF18)\n" +
                          "5. Gerar Relatório Geral (RF20)\n" +
                          "6. Cobrar Taxa (Teste de Exceções)\n" +
                          "7. Adicionar Investimento na Vitrine (RF21)\n" +
                          "8. Remover Investimento da Vitrine (RF21)\n" +
                          "0. Sair\n\n" +
                          "Escolha a opção pretendida:";

            // Caixa de diálogo para entrada de dados
            String opcaoStr = JOptionPane.showInputDialog(null, menu, "Fintech - Área Administrativa", JOptionPane.QUESTION_MESSAGE);

            // Se o utilizador clicar em "Cancelar" ou fechar a janela
            if (opcaoStr == null) {
                break;
            }

            try {
                int opcao = Integer.parseInt(opcaoStr.trim());

                switch (opcao) {
                    case 1:
                        String cpfBusca = JOptionPane.showInputDialog(null, "Introduza o CPF do cliente:", "Consulta de Cliente", JOptionPane.QUESTION_MESSAGE);
                        if (cpfBusca != null && !cpfBusca.trim().isEmpty()) {
                            Cliente clienteFound = consultarCliente(cpfBusca, bancoDeClientes);
                            if (clienteFound != null) {
                                JOptionPane.showMessageDialog(null, clienteFound.visualizarDados(), "Dados do Cliente", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Cliente não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        break;

                    case 2:
                        String numContaBloquear = JOptionPane.showInputDialog(null, "Introduza o número da conta a BLOQUEAR:", "Bloqueio de Conta", JOptionPane.QUESTION_MESSAGE);
                        if (numContaBloquear != null && !numContaBloquear.trim().isEmpty()) {
                            Conta conta = encontrarConta(numContaBloquear.trim(), todasAsContas);
                            if (conta != null) {
                                String justificativa = JOptionPane.showInputDialog(null, "Introduza o motivo/justificativa do bloqueio:", "Justificativa", JOptionPane.QUESTION_MESSAGE);
                                bloquearConta(conta, justificativa);
                                JOptionPane.showMessageDialog(null, "A conta " + numContaBloquear + " foi bloqueada.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Conta não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;

                    case 3:
                        String numContaDesbloquear = JOptionPane.showInputDialog(null, "Introduza o número da conta a DESBLOQUEAR:", "Desbloqueio de Conta", JOptionPane.QUESTION_MESSAGE);
                        if (numContaDesbloquear != null && !numContaDesbloquear.trim().isEmpty()) {
                            Conta conta = encontrarConta(numContaDesbloquear.trim(), todasAsContas);
                            if (conta != null) {
                                desbloquearConta(conta);
                                JOptionPane.showMessageDialog(null, "A conta " + numContaDesbloquear + " foi reativada.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Conta não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;

                    case 4:
                        String numContaExtrato = JOptionPane.showInputDialog(null, "Introduza o número da conta para auditoria de extrato:", "Visualizar Transações", JOptionPane.QUESTION_MESSAGE);
                        if (numContaExtrato != null && !numContaExtrato.trim().isEmpty()) {
                            Conta conta = encontrarConta(numContaExtrato.trim(), todasAsContas);
                            if (conta != null) {
                                // Exibe no console o extrato completo para não sobrecarregar o JOptionPane com texto gigante
                                visualizarTransacoes(conta);
                                JOptionPane.showMessageDialog(null, "Histórico detalhado enviado para a consola do sistema.", "Auditoria Concluída", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Conta não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;

                    case 5:
                        // Chama o método que calcula o montante total
                        gerarRelatorioGeral(todasAsContas, todosOsUsuarios);
                        break;

                    case 6:
                        String numContaTaxa = JOptionPane.showInputDialog(null, "Número da conta para débito de taxa administrativa:", "Cobrança de Taxa", JOptionPane.QUESTION_MESSAGE);
                        if (numContaTaxa != null && !numContaTaxa.trim().isEmpty()) {
                            Conta conta = encontrarConta(numContaTaxa.trim(), todasAsContas);
                            if (conta != null) {
                                String valorStr = JOptionPane.showInputDialog(null, "Introduza o valor da taxa (R$):", "Valor da Taxa", JOptionPane.QUESTION_MESSAGE);
                                if (valorStr != null && !valorStr.trim().isEmpty()) {
                                    double valorTaxa = Double.parseDouble(valorStr.trim());
                                    // Executa o método com os blocos try-catch estruturados anteriormente
                                    cobrarTaxaAdministrativa(conta, valorTaxa);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Conta não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break; 

                    case 7:
                        // Interface para adicionar um novo investimento
                        try {
                            String nomeProd = JOptionPane.showInputDialog(null, "Nome do Produto de Investimento (ex: CDB, Tesouro Direto):");
                            if (nomeProd == null) break;
                            
                            String taxaStr = JOptionPane.showInputDialog(null, "Taxa de Rendimento (% ao mês):");
                            if (taxaStr == null) break;
                            double taxa = Double.parseDouble(taxaStr.replace(",", "."));
                            
                            // Pega a data atual formatada como String para bater com o construtor do colega
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            String dataAtual = sdf.format(new java.util.Date());
                            
                            // Cria o objeto usando o construtor exato da classe Investimento.java
                            Investimento novoInv = new Investimento(nomeProd, taxa, 0.0, dataAtual, "ATIVO");
                            adicionarInvestimento(novoInv, vitrine);
                            
                            JOptionPane.showMessageDialog(null, "Investimento '" + nomeProd + "' cadastrado com sucesso!", "Vitrine Atualizada", JOptionPane.INFORMATION_MESSAGE);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Erro: Digite um valor numérico válido para a taxa.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case 8:
                        // Interface para remover um investimento
                        String idRemoverStr = JOptionPane.showInputDialog(null, "Digite o ID do investimento que deseja remover da vitrine:");
                        if (idRemoverStr != null && !idRemoverStr.trim().isEmpty()) {
                            int idRemover = Integer.parseInt(idRemoverStr.trim());
                            boolean removido = removerInvestimento(idRemover, vitrine);
                            
                            if (removido) {
                                JOptionPane.showMessageDialog(null, "Investimento removido da vitrine.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "ID não encontrado na vitrine.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;

                    case 0:
                        continuar = false;
                        JOptionPane.showMessageDialog(null, "Sessão administrativa terminada.", "Desconectado", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Opção inválida! Selecione uma opção do menu.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Introduza apenas números inteiros para as opções.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método utilitário privado para localizar uma conta na lista global.

    private Conta encontrarConta(String numeroConta, ArrayList<Conta> todasAsContas) {
        for (Conta c : todasAsContas) {
            if (c.numeroConta.equals(numeroConta)) {
                return c;
            }
        }
        return null;
    }
}