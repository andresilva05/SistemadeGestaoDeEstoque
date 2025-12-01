package Repositories;

import Models.ClientePF;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Adicione este import

public class ClientePFRepository<T extends ClientePF> implements IClientePFRepository<T> {
    private List<T> clientes = new ArrayList<>();
    private String ultimaMensagem;
    private int proximoId = 1;

    @Override
    public boolean adicionar(T cliente) {

        cliente.setId(proximoId);
        proximoId++;
        // Verifica documento duplicado
        if (buscarPorDocumento(cliente.getDocumento()) != null) {
            ultimaMensagem = "❌ Já existe cliente com documento " + cliente.getDocumento();
            return false;
        }

        // Valida documento específico
        if (!cliente.validarDocumento()) {
            ultimaMensagem = "❌ Documento inválido: " + cliente.getDocumento();
            return false;
        }

        clientes.add(cliente);
        ultimaMensagem = "✅ Cliente cadastrado com sucesso!";
        return true;
    }

    @Override
    public T buscarPorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id && c.isAtivo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public T buscarPorDocumento(String documento) {
        return clientes.stream()
                .filter(c -> c.getDocumento().equals(documento) && c.isAtivo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> getAll() {
        return clientes.stream()
                .filter(ClientePF::isAtivo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean excluir(int id) {
        T cliente = buscarPorId(id);
        if (cliente != null) {
            cliente.setAtivo(false);
            ultimaMensagem = "✅ Cliente excluído com sucesso!";
            return true;
        }
        ultimaMensagem = "❌ Cliente não encontrado com ID: " + id;
        return false;
    }

    // Método para obter a última mensagem
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}