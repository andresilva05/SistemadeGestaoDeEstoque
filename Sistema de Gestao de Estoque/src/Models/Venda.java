package Models;

import java.math.BigDecimal; // Para precisão decimal em valores monetários
import java.time.LocalDateTime; // Para armazenar data e hora da venda
import java.time.format.DateTimeFormatter; // Para formatar a data/hora

// Classe que representa uma Venda no sistema
public class Venda {
    // Atributos da venda
    private int id;                // Identificador único da venda
    private Cliente cliente;       // Cliente que realizou a compra
    private Produto produto;       // Produto vendido
    private int quantidade;        // Quantidade do produto vendida
    private BigDecimal valorTotal; // Valor total da venda (preço * quantidade)
    private LocalDateTime dataHora; // Data e hora em que a venda foi realizada
    private boolean ativa;         // Status da venda (ativa/cancelada)

    // Construtor principal da classe Venda
    public Venda(int id, Cliente cliente, Produto produto, int quantidade) {
        this.id = id;
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        // Calcula o valor total multiplicando preço pela quantidade
        this.valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        this.dataHora = LocalDateTime.now(); // Define a data/hora atual
        this.ativa = true; // Por padrão, venda é criada como ativa
    }

    // Construtor vazio (padrão) - útil para alguns cenários
    public Venda() {
    }

    // ======= GETTERS E SETTERS =======

    // Retorna o ID da venda
    public int getId() {
        return id;
    }

    // Define o ID da venda
    public void setId(int id) {
        this.id = id;
    }

    // Retorna o cliente da venda
    public Cliente getCliente() {
        return cliente;
    }

    // Define o cliente da venda
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Retorna o produto da venda
    public Produto getProduto() {
        return produto;
    }

    // Define o produto da venda
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    // Retorna a quantidade vendida
    public int getQuantidade() {
        return quantidade;
    }

    // Define a quantidade vendida e recalcula o valor total
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;

        // Recalcula o valor total se a quantidade for alterada e houver um produto associado
        if (produto != null) {
            this.valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        }
    }

    // Retorna o valor total da venda
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    // Define o valor total da venda (uso interno ou específico)
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Retorna a data e hora da venda
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    // Define a data e hora da venda
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    // Retorna se a venda está ativa
    public boolean isAtiva() {
        return ativa;
    }

    // Define o status da venda (ativa/cancelada)
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    // ======= MÉTODO DE FORMATAÇÃO PARA EXIBIÇÃO =======

    @Override
    public String toString() {
        // Formata apenas a data (sem horas) no padrão brasileiro dd/MM/yyyy
        String dataFormatada = dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Retorna uma string formatada com todas as informações da venda
        return "Venda ID: " + id +
                " | Cliente: " + cliente.getNome() +
                " | Produto: " + produto.getNome() +
                " | Quantidade: " + quantidade +
                " | Valor total: R$ " + valorTotal.setScale(2) + // Formata com 2 casas decimais
                " | Data: " + dataFormatada +
                (ativa ? " | ATIVA" : " | CANCELADA"); // Adiciona status da venda
    }
}