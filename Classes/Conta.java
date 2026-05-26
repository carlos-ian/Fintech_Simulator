import java.lang.reflect.Array;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import Exceptions.*;

public abstract class Conta {
    protected ArrayList<Transacao> extrato;
    protected ArrayList<Cartao> cartoes;
    // protected ArrayList<Investimentos> listaInvestimentos;
    // protected Poupanca poupanca;

    protected String numeroConta;
    protected String agencia;
    protected double saldo;
    protected String tipoConta;
    protected Status statusConta;

    Conta(String numeroConta, String agencia, double saldo, String tipoConta, String statusConta) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.statusConta = Status.ATIVO;
    }

    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino) throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

            // 1. Verificação de Cenários: Conta de Origem está Ativa, Conta de Destino Existe e está Ativa
            if (this.statusConta != Status.ATIVO) {
                throw new ContaInativaException("Transação recusada: Sua conta não está ativa.");
            }
            if (destino != null && destino.statusConta != Status.ATIVO) {
                throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");
            }

            // 2. Realização da Transferência
            if (metodoPagamento.equalsIgnoreCase("PIX")) {
                if (this.saldo < valor) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar o PIX.");
                }
                this.saldo -= valor;

            } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
                if (cartaoEscolhido == null) {
                    throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Débito.");
                }
                if (cartaoEscolhido.getEstaBloqueado()) {
                    throw new LimiteInsuficienteException("Transação recusada: O cartão informado está bloqueado.");
                }
                if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                    throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Débito.");
                }

                if (this.saldo < valor) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar o Débito.");
                }
                this.saldo -= valor;

            } else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
                if (cartaoEscolhido == null) {
                    throw new IllegalArgumentException("Transação recusada: É necessário informar o cartão para a função Crédito.");
                }
                if (cartaoEscolhido.getEstaBloqueado()) {
                    throw new LimiteInsuficienteException("Transação recusada: O cartão informado está bloqueado.");
                }
                if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("CREDITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                    throw new IllegalArgumentException("Transação recusada: Este cartão não possui a função Crédito.");
                }
                if (cartaoEscolhido.getLimiteDisponivel() < valor) {
                    throw new LimiteInsuficienteException("Transação Recusada: Limite insuficiente no cartão selecionado.");
                }

                cartaoEscolhido.setLimiteDisponivel(cartaoEscolhido.getLimiteDisponivel() - valor);
                System.out.println("Pagamento no CRÉDITO autorizado.");

            } else {
                throw new IllegalArgumentException("Método de pagamento inválido: " + metodoPagamento);
            }

            // 3. Recebimento da Transferência
            if (destino != null && (metodoPagamento.equalsIgnoreCase("PIX") || metodoPagamento.equalsIgnoreCase("DEBITO"))) {
                destino.saldo += valor;
            }

            LocalDate dataHoje = LocalDate.now();
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataAtual = dataHoje.format(formatadorData);

            LocalTime horaAgora = LocalTime.now();
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            String horaAtual = horaAgora.format(formatadorHora);

            // 4. Instaciação da Transferência
            Transacao transacao = new Transacao(
                    dataAtual,
                    horaAtual,
                    valor,
                    categoria,
                    "SAÍDA",
                    metodoPagamento,
                    "CONCLUIDO",
                    this,
                    destino
            );

            // 5. Adiciona Transação ao Extrato de Ambos
            this.extrato.add(transacao);
            if (destino != null) {
                transacao.setTipoFluxo("ENTRADA");
                destino.extrato.add(transacao);
            }

            System.out.println("Transação registrada com sucesso ID: " + transacao.getId());
            return true;
    }

    public void visualizarExtrato(String fluxo, String metodoPagamento, String categoria, Integer diasPeriodo,  String dataEspecifica) {

        ArrayList<Transacao> transacoesFiltradas = new ArrayList<>();

        // 1. Verificação de Extrato Nulo ou Vazio
        if (this.extrato == null || this.extrato.isEmpty()) {
            System.out.println("Nenhuma transação encontrada para esta conta.");
        }

        // 2. Pega a Data Atual para Cálculo do Período
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoje = LocalDate.now();

        for (Transacao t : this.extrato) {
            boolean filtro = true; // Todas Começam dentro do Filtro
            // Saem do filtro se a opção do filtro não foi selecionada ou se for diferente do filtro selecionado

            // 1. Filtro por Fluxo
            if (fluxo != null && !fluxo.isEmpty() && !t.getFluxo().equalsIgnoreCase(fluxo)) {
                filtro = false;
            }

            // 2. Filtro por Metodo de Pagamento
            if (filtro && metodoPagamento != null && !metodoPagamento.isEmpty() && !t.getMetodoPagamento().equalsIgnoreCase(metodoPagamento)) {
                filtro = false;
            }

            // 3. Filtro por Categoria
            if (filtro && categoria != null && !categoria.isEmpty() && !t.getCategoria().equalsIgnoreCase(categoria)) {
                filtro = false;
            }

            // Tratamento de exceções de data
            try {
                // 4. Filtro por Período (7, 14 ou 30 dias)
                if (filtro && diasPeriodo != null && diasPeriodo > 0) {
                    LocalDate dataTransacao = LocalDate.parse(t.getData(), formatter);
                    long diferencaDias = ChronoUnit.DAYS.between(dataTransacao, hoje);

                    if (diferencaDias < 0 || diferencaDias > diasPeriodo) {
                        filtro = false;
                    }
                }

                // 5. Filtro por Data Específica
                if (filtro && dataEspecifica != null && !dataEspecifica.isEmpty()) {
                    LocalDate dataTransacao = LocalDate.parse(t.getData(), formatter);
                    LocalDate dataBusca = LocalDate.parse(dataEspecifica, formatter);

                    if (!dataTransacao.isEqual(dataBusca)) {
                        filtro = false;
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar data da transação ID: " + t.getId() + ". Desconsiderada.");
                filtro = false;
            }

            if (filtro) {
                transacoesFiltradas.add(t);
            }
        }

        System.out.println("\n========== EXTRATO DE CONTA ==========");
        if (transacoesFiltradas.isEmpty()) {
            System.out.println("Nenhuma transação corresponde aos critérios.");
        } else {
            for (Transacao t : transacoesFiltradas) {
                t.visualizarTransacao();
            }
        }
        System.out.println("======================================\n");
    }

}