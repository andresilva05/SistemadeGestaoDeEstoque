package Repositories;

import Models.Venda;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class VendasRepository implements IVendasRepository {

    private List<Venda> vendas = new ArrayList<>();
    private String ultimaMensagem;
    private int proximoId = 1;

    // ================================
    // ADICIONAR VENDA
    // ================================
    @Override
    public boolean adicionar(Venda venda) {
        if (venda == null) {
            ultimaMensagem = "❌ Venda inválida.";
            return false;
        }

        venda.setId(proximoId);
        proximoId++;

        vendas.add(venda);
        ultimaMensagem = "✅ Venda registrada com sucesso!";
        return true;
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @Override
    public Venda buscarPorId(int id) {
        return vendas.stream()
                .filter(v -> v.getId() == id && v.isAtiva())
                .findFirst()
                .orElse(null);
    }

    // ================================
    // LISTAR TODAS AS VENDAS ATIVAS
    // ================================
    @Override
    public List<Venda> getAll() {
        return vendas.stream()
                .filter(Venda::isAtiva)
                .collect(Collectors.toList());
    }

    // ================================
    // EXCLUSÃO (CANCELAMENTO DE VENDA)
    // ================================
    @Override
    public boolean cancelar(int id) {
        Venda venda = buscarPorId(id);

        if (venda != null) {
            venda.setAtiva(false);
            ultimaMensagem = "Venda ID " + id + " cancelada.";
            return true;
        }

        ultimaMensagem = "Venda não encontrada com ID: " + id;
        return false;
    }

    @Override
    public boolean produtoTemVendas(int idProduto) {
        List<Venda> todasVendas = getAll();

        for (Venda venda : todasVendas) {
            // IMPORTANTE: Verificar se produto não é null
            if (venda.getProduto() != null && venda.getProduto().getId() == idProduto) {
                System.out.println("ENCONTROU: Venda ID " + venda.getId() +
                        " tem produto ID " + idProduto);
                return true;
            }
        }

        System.out.println("NÃO ENCONTROU vendas para produto ID " + idProduto);
        return false;
    }

    // ================================
    // BUSCAR VENDAS DE UM CLIENTE
    // ================================
    @Override
    public List<Venda> buscarPorCliente(int idCliente) {
        return vendas.stream()
                .filter(v -> v.isAtiva() && v.getCliente().getId() == idCliente)
                .collect(Collectors.toList());
    }


    @Override
    public boolean clienteTemVendas(int idCliente) {
        List<Venda> todasVendas = getAll();

        for (Venda venda : todasVendas) {
            if (venda.getCliente() != null && venda.getCliente().getId() == idCliente) {
                return true;
            }
        }

        return false;
    }

    // ================================
    // BUSCAR VENDAS DE UM PRODUTO
    // ================================
    @Override
    public List<Venda> buscarPorProduto(int idProduto) {
        return vendas.stream()
                .filter(v -> v.isAtiva() && v.getProduto().getId() == idProduto)
                .collect(Collectors.toList());
    }

    // ================================
    // MÉTODO OPCIONAL PARA LISTAR
    // ================================
    public void listar() {
        List<Venda> ativas = getAll();

        if (ativas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            ativas.forEach(System.out::println);
        }
    }

    // ================================
    // ÚLTIMA MENSAGEM DO REPOSITÓRIO
    // ================================
    @Override
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}
