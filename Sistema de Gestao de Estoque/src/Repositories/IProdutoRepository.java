package Repositories;

import Models.Produto;

import java.math.BigDecimal;
import java.util.List;

// Interface que define o contrato para o repositório de produtos
public interface IProdutoRepository {

    // Adiciona um novo produto ao repositório
    boolean adicionar(Produto produto);

    // Busca um produto pelo seu ID
    Produto buscarPorId(int id);

    // Busca produtos que contenham parte do nome especificado
    List<Produto> buscarPorNome(String parteNome);

    // Retorna todos os produtos ativos
    List<Produto> getAll();

    // Retorna produtos com estoque baixo (abaixo do valor limite)
    List<Produto> estoqueBaixo(int valorLimite);

    // Exclui logicamente um produto (marca como inativo)
    boolean excluir(int id);

    // Retorna a última mensagem gerada pelo repositório
    String getUltimaMensagem();

    // Atualiza a quantidade em estoque de um produto
    boolean atualizarEstoque(int id, int novaQuantidade);

    // Atualiza o preço de um produto
    boolean atualizarPreco(int id, BigDecimal novoPreco);
}