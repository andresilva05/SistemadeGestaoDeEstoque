package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venda {
    private int id;
    private Cliente cliente;
    private Produto produto;
    private int quantidade;
    private BigDecimal valorTotal;
    private LocalDateTime dataHora;
    private boolean ativa;

    public Venda(int id, Cliente cliente, Produto produto, int quantidade) {
        this.id = id;
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        this.dataHora = LocalDateTime.now();
        this.ativa = true;
    }

    public Venda() {
    }

    // ======= GETTERS E SETTERS =======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;

        // recalcular valor total se a quantidade mudar
        if (produto != null) {
            this.valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        }
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    // ======= FORMATAÇÃO DE IMPRESSÃO =======

    @Override
    public String toString() {
        // Formatando apenas a data (sem horas)
        String dataFormatada = dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return "Venda ID: " + id +
                " | Cliente: " + cliente.getNome() +
                " | Produto: " + produto.getNome() +
                " | Quantidade: " + quantidade +
                " | Valor total: R$ " + valorTotal.setScale(2) +
                " | Data: " + dataFormatada +
                (ativa ? " | ATIVA" : " | CANCELADA");
    }
}