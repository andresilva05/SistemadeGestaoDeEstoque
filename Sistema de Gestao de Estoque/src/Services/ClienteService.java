package Services;

import Models.Cliente;
import Repositories.IClienteRepository;
import Repositories.IVendasRepository;  // ⬅️ NOVO IMPORT

import java.util.List;

public class ClienteService<T extends Cliente> {

    private IClienteRepository<T> repository;
    private IVendasRepository vendasRepository;  // ⬅️ NOVA DEPENDÊNCIA
    private String ultimaMensagem;

    // ⬅️ CONSTRUTOR MODIFICADO
    public ClienteService(IClienteRepository<T> repository, IVendasRepository vendasRepository) {
        this.repository = repository;
        this.vendasRepository = vendasRepository;  // ⬅️ NOVO
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
        //Verificar se cliente existe
        T cliente = repository.buscarPorId(id);

        if (cliente == null) {
            ultimaMensagem = "❌ Cliente com ID " + id + " não encontrado!";
            return false;
        }

        //Verificar se cliente tem vendas
        if (vendasRepository.clienteTemVendas(id)) {
            ultimaMensagem = "❌ Não é possível excluir cliente \"" + cliente.getNome() +
                    "\" - possui vendas registradas!";
            return false;
        }

        // ⬅️ TERCEIRO: Se não tem vendas, excluir
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