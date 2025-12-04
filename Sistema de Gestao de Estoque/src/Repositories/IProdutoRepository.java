package Repositories;

import Models.Produto;

import java.math.BigDecimal;
import java.util.List;

public interface IProdutoRepository {
    boolean adicionar(Produto produto);

    Produto buscarPorId(int id);

    List<Produto> buscarPorNome(String parteNome);

    List<Produto> getAll();

    boolean excluir(int id);

    String getUltimaMensagem(); // Para mensagens

    // Adicione estes métodos se não existirem:
    boolean atualizarEstoque(int id, int novaQuantidade);

    boolean atualizarPreco(int id, BigDecimal novoPreco);
}