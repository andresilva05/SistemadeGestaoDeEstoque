package Services;

import Models.Cliente;
import Repositories.IClienteRepository;
import Repositories.IVendasRepository;  // ⬅️ Import necessário para verificar vendas dos clientes

import java.util.List;

// Classe de serviço para gerenciar operações relacionadas a clientes
// <T extends Cliente> significa que trabalha com qualquer tipo que seja Cliente ou suas subclasses
public class ClienteService<T extends Cliente> {

    private IClienteRepository<T> repository;        // Repositório para operações de CRUD de clientes
    private IVendasRepository vendasRepository;     //  verificar vendas associadas
    private String ultimaMensagem;                   // Armazena a última mensagem gerada pelo serviço

    // ⬅️ CONSTRUTOR MODIFICADO: agora recebe também o repositório de vendas
    public ClienteService(IClienteRepository<T> repository, IVendasRepository vendasRepository) {
        this.repository = repository;
        this.vendasRepository = vendasRepository;  // inicializa o repositório de vendas
    }

    // ================================
    // CADASTRAR CLIENTE
    // ================================
    public boolean cadastrarCliente(T cliente) {
        ultimaMensagem = null;  // Limpa mensagem anterior

        // Delega a operação para o repositório
        boolean sucesso = repository.adicionar(cliente);

        if (!sucesso) {
            // Se falhou, pega a mensagem de erro do repositório
            ultimaMensagem = repository.getUltimaMensagem();
        } else {
            // Se teve sucesso, define mensagem positiva
            ultimaMensagem = "✅ Cliente cadastrado com sucesso!";
        }

        return sucesso;  // Retorna o resultado da operação
    }

    // ================================
    // BUSCAR CLIENTE POR ID
    // ================================
    public T buscarPorId(int id) {
        // Delega a busca para o repositório
        return repository.buscarPorId(id);
    }

    // ================================
    // EXCLUIR CLIENTE
    // ================================
    public boolean excluirCliente(int id) {
        // Primeiro passo: Verificar se cliente existe
        T cliente = repository.buscarPorId(id);

        if (cliente == null) {
            ultimaMensagem = "❌ Cliente com ID " + id + " não encontrado!";
            return false;  // Falha: cliente não existe
        }

        // Segundo passo: Verificar se cliente tem vendas registradas
        // Usa o vendasRepository para verificar regra de negócio
        if (vendasRepository.clienteTemVendas(id)) {
            ultimaMensagem = "❌ Não é possível excluir cliente \"" + cliente.getNome() +
                    "\" - possui vendas registradas!";
            return false;  // Falha: cliente tem vendas associadas
        }

        // ⬅️ TERCEIRO PASSO: Se não tem vendas, pode excluir
        boolean sucesso = repository.excluir(id);

        if (sucesso) {
            // Se excluiu com sucesso, pega mensagem do repositório
            ultimaMensagem = repository.getUltimaMensagem();
        } else {
            // Se falhou na exclusão, define mensagem de erro
            ultimaMensagem = "❌ Falha ao excluir cliente com ID: " + id;
        }

        return sucesso;  // Retorna o resultado da operação
    }

    // ================================
    // LISTAR TODOS OS CLIENTES
    // ================================
    public List<T> listarTodos() {
        // Obtém todos os clientes do repositório
        List<T> clientes = repository.getAll();

        // Define mensagem apropriada baseada no resultado
        if (clientes.isEmpty()) {
            ultimaMensagem = "📝 Nenhum cliente cadastrado.";
        } else {
            ultimaMensagem = "📋 Lista de clientes carregada.";
        }

        return clientes;  // Retorna a lista de clientes
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM
    // ================================
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}