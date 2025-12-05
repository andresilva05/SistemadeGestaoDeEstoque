package Services;

import Models.Cliente;
import Repositories.IClienteRepository;

import java.util.List;

public class ClienteService<T extends Cliente> {

    private IClienteRepository<T> repository;
    private String ultimaMensagem;

    public ClienteService(IClienteRepository<T> repository) {
        this.repository = repository;
    }

    public boolean cadastrarCliente(T cliente) {
        ultimaMensagem = null;

        boolean sucesso = repository.adicionar(cliente);

        if (!sucesso) {
            ultimaMensagem = repository.getUltimaMensagem();
        } else {
            ultimaMensagem = "✅ Cliente cadastrado com sucesso!";
        }

        return sucesso;
    }

    public T buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public boolean excluirCliente(int id) {
        boolean sucesso = repository.excluir(id);

        if (sucesso) {
            ultimaMensagem = repository.getUltimaMensagem();
        } else {
            ultimaMensagem = "❌ Falha ao excluir cliente com ID: " + id;
        }

        return sucesso;
    }

    public List<T> listarTodos() {
        List<T> clientes = repository.getAll();

        if (clientes.isEmpty()) {
            ultimaMensagem = "📝 Nenhum cliente cadastrado.";
        } else {
            ultimaMensagem = "📋 Lista de clientes carregada.";
        }

        return clientes;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}
