package Repositories;

import Models.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Implementação do repositório de clientes usando generics
// <T extends Cliente> significa que esta classe funciona com qualquer tipo que seja Cliente ou suas subclasses
public class ClienteRepository<T extends Cliente> implements IClienteRepository<T> {

    // Lista para armazenar os clientes em memória
    private List<T> clientes = new ArrayList<>();

    // Armazena a última mensagem gerada pelas operações
    private String ultimaMensagem;

    // Contador para gerar IDs automáticos e sequenciais
    private static int proximoIdGlobal = 1;

    // ================================
    // ADICIONAR CLIENTE
    // ================================
    @Override
    public boolean adicionar(T cliente) {
        // Atribui um ID automático ao cliente
        cliente.setId(proximoIdGlobal++);// Incrementa para o próximo cliente

        // Verificar duplicidade - não permite dois clientes com o mesmo documento
        if (buscarPorDocumento(cliente.getDocumento()) != null) {
            ultimaMensagem = "❌ Já existe cliente com documento: " + cliente.getDocumento();
            return false; // Falha na operação
        }

        // Valida o documento usando o método abstrato da classe Cliente
        if (!cliente.validarDocumento()) {
            ultimaMensagem = "❌ Documento inválido: " + cliente.getDocumento();
            return false; // Falha na operação
        }

        // Adiciona o cliente à lista
        clientes.add(cliente);
        ultimaMensagem = "✅ Cliente cadastrado com sucesso!";
        return true; // Sucesso na operação
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @Override
    public T buscarPorId(int id) {
        // Usa Stream API para buscar um cliente pelo ID e que esteja ativo
        return clientes.stream()
                .filter(c -> c.getId() == id && c.isAtivo()) // Filtra por ID e status ativo
                .findFirst() // Retorna o primeiro que encontrar
                .orElse(null); // Retorna null se não encontrar
    }

    // ================================
    // BUSCAR POR DOCUMENTO
    // ================================
    @Override
    public T buscarPorDocumento(String documento) {
        // Busca um cliente pelo documento e que esteja ativo
        return clientes.stream()
                .filter(c -> c.getDocumento().equals(documento) && c.isAtivo())
                .findFirst()
                .orElse(null);
    }

    // ================================
    // LISTAR TODOS OS CLIENTES ATIVOS
    // ================================
    @Override
    public List<T> getAll() {
        // Retorna uma nova lista contendo apenas os clientes ativos
        return clientes.stream()
                .filter(Cliente::isAtivo) // Referência de método para filtrar ativos
                .collect(Collectors.toList()); // Converte o Stream para List
    }

    // ================================
    // EXCLUIR CLIENTE (EXCLUSÃO LÓGICA)
    // ================================
    @Override
    public boolean excluir(int id) {
        // Busca o cliente pelo ID
        T cliente = buscarPorId(id);

        if (cliente != null) {
            // Faz exclusão lógica - marca como inativo ao invés de remover da lista
            cliente.setAtivo(false);
            ultimaMensagem = "✅ Cliente excluído com sucesso!";
            return true; // Sucesso na operação
        }

        // Cliente não encontrado
        ultimaMensagem = "❌ Cliente não encontrado com ID: " + id;
        return false; // Falha na operação
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM
    // ================================
    @Override
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}