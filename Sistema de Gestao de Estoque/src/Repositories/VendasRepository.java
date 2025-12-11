package Repositories;

import Models.Venda;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Implementação do repositório de vendas
public class VendasRepository implements IVendasRepository {

    // Lista para armazenar as vendas em memória
    private List<Venda> vendas = new ArrayList<>();

    // Armazena a última mensagem gerada pelas operações
    private String ultimaMensagem;

    // Contador para gerar IDs automáticos e sequenciais
    private int proximoId = 1;

    // ================================
    // ADICIONAR VENDA
    // ================================
    @Override
    public boolean adicionar(Venda venda) {
        // Valida se a venda não é nula
        if (venda == null) {
            ultimaMensagem = "❌ Venda inválida.";
            return false; // Falha na operação
        }

        // Atribui um ID automático à venda
        venda.setId(proximoId);
        proximoId++; // Incrementa para a próxima venda

        // Adiciona a venda à lista
        vendas.add(venda);
        ultimaMensagem = "✅ Venda registrada com sucesso!";
        return true; // Sucesso na operação
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @Override
    public Venda buscarPorId(int id) {
        // Usa Stream API para buscar uma venda pelo ID e que esteja ativa
        return vendas.stream()
                .filter(v -> v.getId() == id && v.isAtiva()) // Filtra por ID e status ativo
                .findFirst() // Retorna o primeiro que encontrar
                .orElse(null); // Retorna null se não encontrar
    }

    // ================================
    // LISTAR TODAS AS VENDAS ATIVAS
    // ================================
    @Override
    public List<Venda> getAll() {
        // Retorna uma nova lista contendo apenas as vendas ativas
        return vendas.stream()
                .filter(Venda::isAtiva) // Referência de método para filtrar ativas
                .collect(Collectors.toList()); // Converte o Stream para List
    }

    // ================================
    // CANCELAR VENDA (EXCLUSÃO LÓGICA)
    // ================================
    @Override
    public boolean cancelar(int id) {
        // Busca a venda pelo ID
        Venda venda = buscarPorId(id);

        if (venda != null) {
            // Faz exclusão lógica - marca como cancelada ao invés de remover da lista
            venda.setAtiva(false);
            ultimaMensagem = "Venda ID " + id + " cancelada.";
            return true; // Sucesso na operação
        }

        // Venda não encontrada
        ultimaMensagem = "Venda não encontrada com ID: " + id;
        return false; // Falha na operação
    }

    // ================================
    // VERIFICAR SE PRODUTO TEM VENDAS
    // ================================
    @Override
    public boolean produtoTemVendas(int idProduto) {
        // Obtém todas as vendas ativas
        List<Venda> todasVendas = getAll();

        // Percorre as vendas procurando o produto especificado
        for (Venda venda : todasVendas) {
            // IMPORTANTE: Verificar se produto não é null para evitar NullPointerException
            if (venda.getProduto() != null && venda.getProduto().getId() == idProduto) {
                // Debug: mostra quando encontra uma venda com o produto
                System.out.println("ENCONTROU: Venda ID " + venda.getId() +
                        " tem produto ID " + idProduto);
                return true; // Produto tem vendas associadas
            }
        }

        // Debug: mostra quando não encontra vendas para o produto
        System.out.println("NÃO ENCONTROU vendas para produto ID " + idProduto);
        return false; // Produto não tem vendas associadas
    }

    // ================================
    // BUSCAR VENDAS DE UM CLIENTE
    // ================================
    @Override
    public List<Venda> buscarPorCliente(int idCliente) {
        // Busca vendas ativas realizadas por um cliente específico
        return vendas.stream()
                .filter(v -> v.isAtiva() && v.getCliente().getId() == idCliente)
                .collect(Collectors.toList());
    }

    // ================================
    // VERIFICAR SE CLIENTE TEM VENDAS
    // ================================
    @Override
    public boolean clienteTemVendas(int idCliente) {
        // Obtém todas as vendas ativas
        List<Venda> todasVendas = getAll();

        // Percorre as vendas procurando o cliente especificado
        for (Venda venda : todasVendas) {
            // Verifica se cliente não é null e tem o ID especificado
            if (venda.getCliente() != null && venda.getCliente().getId() == idCliente) {
                return true; // Cliente tem vendas associadas
            }
        }

        return false; // Cliente não tem vendas associadas
    }

    // ================================
    // BUSCAR VENDAS DE UM PRODUTO
    // ================================
    @Override
    public List<Venda> buscarPorProduto(int idProduto) {
        // Busca vendas ativas que contenham um produto específico
        return vendas.stream()
                .filter(v -> v.isAtiva() && v.getProduto().getId() == idProduto)
                .collect(Collectors.toList());
    }

    // ================================
    // LISTAR VENDAS (MÉTODO OPCIONAL)
    // ================================
    // Método para listar vendas no console
    public void listar() {
        // Obtém todas as vendas ativas
        List<Venda> ativas = getAll();

        if (ativas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            // Usa forEach com referência de método para imprimir cada venda
            ativas.forEach(System.out::println);
        }
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM
    // ================================
    @Override
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}