package Repositories;

import java.util.List;

// Interface que define o contrato para o repositório de clientes
// <T> é um tipo genérico que deve ser uma subclasse de Cliente (ex: ClientePF ou ClientePJ)
public interface IClienteRepository<T> {

    // Adiciona um novo cliente ao repositório
    boolean adicionar(T cliente);

    // Busca um cliente pelo seu ID
    T buscarPorId(int id);

    // Busca um cliente pelo seu documento (CPF ou CNPJ)
    T buscarPorDocumento(String documento);

    // Retorna todos os clientes ativos
    List<T> getAll();

    // Exclui logicamente um cliente (marca como inativo)
    boolean excluir(int id);

    // Retorna a última mensagem gerada pelo repositório
    String getUltimaMensagem();
}