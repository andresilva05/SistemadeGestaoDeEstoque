package Services;

import Models.ClientePF;
import Repositories.IClientePFRepository;

import java.util.List;

public class ClientePFService<T extends ClientePF> {
    private IClientePFRepository<T> repository;
    private String ultimaMensagem;

    public ClientePFService(IClientePFRepository<T> repository) {
        this.repository = repository;
    }

    public boolean cadastrarCliente(T cliente) {
        // Limpa mensagem anterior
        ultimaMensagem = null;

        boolean sucesso = repository.adicionar(cliente);

        // Se não houve sucesso, pega a mensagem de erro do repositório
        if (!sucesso) {
            ultimaMensagem = repository.getUltimaMensagem();
        } else {
            ultimaMensagem = "✅ Cliente cadastrado com sucesso!";
        }

        return sucesso;
    }

    public T buscarPorId(int id) {
        T cliente = repository.buscarPorId(id);
        return cliente;
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

    // Método para obter a última mensagem
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}