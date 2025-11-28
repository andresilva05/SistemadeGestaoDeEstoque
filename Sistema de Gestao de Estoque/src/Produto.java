import java.math.BigDecimal;
import java.util.Scanner;

public class Produto {
    private int id;
    private String nome;
    private BigDecimal preco;
    private int qtdEstoque;
    private boolean ativo;


    public Produto(int id, String nome, BigDecimal preco, int qtdEstoque, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
        this.ativo = ativo;
    }

    public Produto() {
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        String estoqueFormatado = (qtdEstoque == 0) ? "Sem estoque" : String.valueOf(qtdEstoque);

        return "ID: " + id +
                " | Nome: " + nome +
                " | Estoque: " + estoqueFormatado +
                " | Preço: R$ " + preco.setScale(2);
    }

}
