package Repositories;

import Models.Venda;

import java.util.List;

// Interface que define o contrato para o repositório de vendas
public interface IVendasRepository {

    // Adiciona uma nova venda ao repositório
    boolean adicionar(Venda venda);

    // Busca uma venda pelo seu ID
    Venda buscarPorId(int id);

    // Retorna todas as vendas ativas
    List<Venda> getAll();

    // Verifica se um cliente tem vendas registradas
    boolean clienteTemVendas(int idCliente);

    // Cancela uma venda (exclusão lógica)
    boolean cancelar(int id);

    // Verifica se um produto tem vendas registradas
    boolean produtoTemVendas(int idProduto);

    // Busca vendas realizadas por um cliente específico
    List<Venda> buscarPorCliente(int idCliente);

    // Busca vendas que contenham um produto específico
    List<Venda> buscarPorProduto(int idProduto);

    // Retorna a última mensagem gerada pelo repositório
    String getUltimaMensagem();
}