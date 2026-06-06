package Classes.Model.Conta;

import Classes.Model.Operacoes.Cartao;
import Classes.Exceptions.*;
import Classes.Model.Operacoes.Status;
import Classes.Model.Operacoes.Transacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaCorrente extends Conta {
    private double limiteChequeEspecial;

    public ContaCorrente(String numeroConta, String agencia, double saldo, String tipoConta, double limiteChequeEspecial) {
        super(numeroConta, agencia, saldo, tipoConta);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    public boolean realizarTransacao(double valor, String metodoPagamento, Cartao cartaoEscolhido, String categoria, Conta destino)
            throws ContaInativaException, SaldoInsuficienteException, LimiteInsuficienteException {

        if (this.statusConta != Status.ATIVO) {throw new ContaInativaException("Transação recusada: Sua conta não está ativa.");}
        if (destino != null && !destino.getStatus().equals(Status.ATIVO)) {throw new ContaInativaException("Transação recusada: A conta de destino não está ativa.");}
        if (destino == null) {throw new IllegalArgumentException("Conta de Destino não existe");}

        if (metodoPagamento.equalsIgnoreCase("PIX")) {
            if (this.saldo + this.limiteChequeEspecial < valor) {
                throw new SaldoInsuficienteException("Saldo e limite de cheque especial insuficientes.");
            }
            this.saldo -= valor;
            destino.saldo += valor;


        } else if (metodoPagamento.equalsIgnoreCase("DEBITO")) {
            if (cartaoEscolhido == null) throw new IllegalArgumentException("Informe o cartão.");
            if (cartaoEscolhido.getEstaBloqueado()) throw new IllegalArgumentException("Cartão bloqueado.");
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("DEBITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Este cartão não possui a função Débito.");
            }

            if (this.saldo + this.limiteChequeEspecial < valor) {
                throw new SaldoInsuficienteException("Saldo e limite de cheque especial insuficientes.");
            }
            this.saldo -= valor;
            destino.saldo += valor;

        } else if (metodoPagamento.equalsIgnoreCase("CREDITO")) {
            if (cartaoEscolhido == null) throw new IllegalArgumentException("Informe o cartão.");
            if (cartaoEscolhido.getEstaBloqueado()) throw new LimiteInsuficienteException("Cartão bloqueado.");
            if (!cartaoEscolhido.getTipoCartao().equalsIgnoreCase("CREDITO") && !cartaoEscolhido.getTipoCartao().equalsIgnoreCase("AMBOS")) {
                throw new IllegalArgumentException("Este cartão não possui a função Crédito.");
            }

            if (cartaoEscolhido.getLimiteDisponivel() < valor) {
                throw new LimiteInsuficienteException("Limite insuficiente no cartão.");
            }
            cartaoEscolhido.setLimiteDisponivel(cartaoEscolhido.getLimiteDisponivel() - valor);
            destino.saldo += valor;

        } else {
            throw new IllegalArgumentException("Método de pagamento inválido: " + metodoPagamento);
        }

        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transacao transacao = new Transacao(dataAtual, horaAtual, valor, categoria, "SAÍDA", metodoPagamento, "CONCLUIDO", this, destino);
        this.extrato.add(transacao);
        Transacao transacaoEntrada = new Transacao(dataAtual, horaAtual, valor, categoria, "ENTRADA", metodoPagamento, "CONCLUIDO", destino, this);
        destino.extrato.add(transacaoEntrada);

        return true;
    }

    @Override
    public String visualizarDadosConta() {
        return super.visualizarDadosConta() +
                String.format("Limite Cheque Especial: R$ %.2f\n", this.limiteChequeEspecial);
    }

    public double getLimiteChequeEspecial() {return this.limiteChequeEspecial;}
}