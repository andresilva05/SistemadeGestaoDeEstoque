package Repositories;

import Models.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoRepository implements IProdutoRepository {

    private List<Produto> produtos = new ArrayList<>();
    private String ultimaMensagem;
    private int proximoId = 1;

    @Override
    public boolean adicionar(Produto produto) {


        produto.setId(proximoId);
        proximoId++;

        // Verificar nome duplicado
        if (buscarPorNome(produto.getNome()) != null) {
            ultimaMensagem = "❌ Já existe produto com nome '" + produto.getNome() + "'";
            return false;
        }

        produtos.add(produto);
        ultimaMensagem = "✅ Produto adicionado com sucesso!";
        return true;
    }

    @Override
    public Produto buscarPorId(int id) {
        return produtos.stream()
                .filter(p -> p.getId() == id && p.isAtivo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Produto buscarPorNome(String nome) {
        return produtos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome) && p.isAtivo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Produto> getAll() {
        return produtos.stream()
                .filter(Produto::isAtivo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean excluir(int id) {
        Produto produto = buscarPorId(id);
        if (produto != null) {
            produto.setAtivo(false);
            ultimaMensagem = "✅ Produto excluído com sucesso!";
            return true;
        }
        ultimaMensagem = "❌ Produto não encontrado com ID: " + id;
        return false;
    }

    @Override
    public boolean atualizarEstoque(int id, int novaQuantidade) {
        Produto produto = buscarPorId(id);
        if (produto != null) {
            produto.setQtdEstoque(novaQuantidade);
            ultimaMensagem = "✅ Estoque atualizado para " + novaQuantidade;
            return true;
        }
        ultimaMensagem = "❌ Produto não encontrado com ID: " + id;
        return false;
    }

    @Override
    public boolean atualizarPreco(int id, BigDecimal novoPreco) {
        Produto produto = buscarPorId(id);
        if (produto != null && novoPreco != null && novoPreco.compareTo(BigDecimal.ZERO) > 0) {
            produto.setPreco(novoPreco);
            ultimaMensagem = "✅ Preço atualizado para R$ " + novoPreco;
            return true;
        }
        ultimaMensagem = "❌ Falha ao atualizar preço";
        return false;
    }

    // Método para listar produtos (opcional, mantém compatibilidade)
    public void listar() {
        List<Produto> ativos = getAll();
        if (ativos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            ativos.forEach(System.out::println);
        }
    }

    @Override
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

}
