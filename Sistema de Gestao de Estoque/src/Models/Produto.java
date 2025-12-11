package Models;

import java.math.BigDecimal; // Importa BigDecimal para precisão decimal em valores monetários

// Classe que representa um Produto no sistema
public class Produto {
    // Atributos do produto
    private int id;                // Identificador único do produto
    private String nome;           // Nome do produto
    private BigDecimal preco;      // Preço do produto (usando BigDecimal para precisão)
    private int qtdEstoque;        // Quantidade disponível em estoque
    private boolean ativo;         // Status (ativo/inativo) do produto

    // Construtor principal da classe Produto
    public Produto(int id, String nome, BigDecimal preco, int qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
        this.ativo = true; // Por padrão, produto é criado como ativo
    }

    // Construtor vazio (padrão) - útil para alguns cenários como inicialização
    public Produto() {
    }

    // ======= GETTERS E SETTERS =======

    // Define o ID do produto
    public void setId(int id) {
        this.id = id;
    }

    // Retorna o ID do produto
    public int getId() {
        return id;
    }

    // Define o nome do produto
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna o nome do produto
    public String getNome() {
        return nome;
    }

    // Define o preço do produto
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    // Retorna o preço do produto
    public BigDecimal getPreco() {
        return preco;
    }

    // Define a quantidade em estoque do produto
    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    // Retorna a quantidade em estoque do produto
    public int getQtdEstoque() {
        return qtdEstoque;
    }

    // Define o status do produto (ativo/inativo)
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // Retorna se o produto está ativo
    public boolean isAtivo() {
        return ativo;
    }

    // ======= MÉTODO DE FORMATAÇÃO PARA EXIBIÇÃO =======

    @Override
    public String toString() {
        // Formata a mensagem de estoque: mostra "Sem estoque" se quantidade for zero
        String estoqueFormatado = (qtdEstoque == 0) ? "Sem estoque" : String.valueOf(qtdEstoque);

        // Retorna uma string formatada com todas as informações do produto
        return "ID: " + id +
                " | Nome: " + nome +
                " | Estoque: " + estoqueFormatado +
                " | Preço: R$ " + preco.setScale(2); // Formata o preço com 2 casas decimais
    }
}