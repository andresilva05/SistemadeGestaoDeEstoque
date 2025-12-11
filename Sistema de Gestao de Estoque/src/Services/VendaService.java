package Services;

import Models.Cliente;
import Models.ClientePF;
import Models.ClientePJ;
import Models.Produto;
import Models.Venda;

import Repositories.IVendasRepository;
import Repositories.IClienteRepository;
import Repositories.IProdutoRepository;

import java.math.BigDecimal;
import java.util.List;

// Classe de serviço para gerenciar operações relacionadas a vendas
public class VendaService {

    private IVendasRepository vendaRepository;           // Repositório de vendas
    private IClienteRepository<ClientePF> clientePFRepository;   // Repositório de clientes PF
    private IClienteRepository<ClientePJ> clientePJRepository;   // Repositório de clientes PJ
    private IProdutoRepository produtoRepository;        // Repositório de produtos
    private String ultimaMensagem;                       // Armazena a última mensagem gerada pelo serviço

    // Construtor que recebe todas as dependências necessárias
    public VendaService(IVendasRepository vendaRepository,
                        IClienteRepository<ClientePF> clientePFRepository,
                        IClienteRepository<ClientePJ> clientePJRepository,
                        IProdutoRepository produtoRepository) {

        this.vendaRepository = vendaRepository;
        this.clientePFRepository = clientePFRepository;
        this.clientePJRepository = clientePJRepository;
        this.produtoRepository = produtoRepository;
        this.ultimaMensagem = "";  // Inicializa mensagem vazia
    }

    // ================================
    // REGISTRAR VENDA
    // ================================
    public boolean registrarVenda(int idCliente, int idProduto, int quantidade) {

        // Primeira validação: quantidade deve ser positiva
        if (quantidade <= 0) {
            ultimaMensagem = "❌ Quantidade deve ser positiva.";
            return false;  // Falha: quantidade inválida
        }

        // Buscar cliente - tenta primeiro PF, depois PJ
        Cliente cliente = clientePFRepository.buscarPorId(idCliente);
        if (cliente == null) {
            cliente = clientePJRepository.buscarPorId(idCliente);
        }

        if (cliente == null) {
            ultimaMensagem = "❌ Cliente não encontrado com ID: " + idCliente;
            return false;  // Falha: cliente não encontrado
        }

        // Buscar produto
        Produto produto = produtoRepository.buscarPorId(idProduto);
        if (produto == null) {
            ultimaMensagem = "❌ Produto não encontrado com ID: " + idProduto;
            return false;  // Falha: produto não encontrado
        }

        // Validar produto ativo
        if (!produto.isAtivo()) {
            ultimaMensagem = "❌ Produto não está ativo.";
            return false;  // Falha: produto inativo
        }

        // Validar estoque disponível
        if (produto.getQtdEstoque() < quantidade) {
            ultimaMensagem = "❌ Estoque insuficiente. Disponível: " + produto.getQtdEstoque();
            return false;  // Falha: estoque insuficiente
        }

        // ATUALIZAR ESTOQUE NO REPOSITÓRIO (diminuir estoque)
        int novoEstoque = produto.getQtdEstoque() - quantidade;
        boolean estoqueAtualizado = produtoRepository.atualizarEstoque(idProduto, novoEstoque);

        if (!estoqueAtualizado) {
            ultimaMensagem = "❌ Falha ao atualizar estoque.";
            return false;  // Falha: não conseguiu atualizar estoque
        }

        // Atualizar o objeto produto localmente para refletir o novo estoque
        produto.setQtdEstoque(novoEstoque);

        // Criar objeto Venda (ID 0 será substituído pelo repositório)
        Venda novaVenda = new Venda(0, cliente, produto, quantidade);

        // Registrar venda no repositório
        boolean vendaRegistrada = vendaRepository.adicionar(novaVenda);

        if (vendaRegistrada) {
            ultimaMensagem = "✅ Venda registrada com sucesso!";
        } else {
            ultimaMensagem = vendaRepository.getUltimaMensagem();  // Pega mensagem de erro do repositório
        }

        return vendaRegistrada;  // Retorna resultado da operação
    }

    // ================================
    // LISTAR TODAS AS VENDAS
    // ================================
    public List<Venda> listarVendas() {
        // Delega para o repositório
        return vendaRepository.getAll();
    }

    // ================================
    // BUSCAR VENDAS POR CLIENTE
    // ================================
    public List<Venda> buscarVendasPorCliente(int idCliente) {
        // Delega para o repositório
        return vendaRepository.buscarPorCliente(idCliente);
    }

    // ================================
    // BUSCAR VENDAS POR PRODUTO
    // ================================
    public List<Venda> buscarVendasPorProduto(int idProduto) {
        // Delega para o repositório
        return vendaRepository.buscarPorProduto(idProduto);
    }

    // ================================
    // CANCELAR VENDA
    // ================================
    public boolean cancelarVenda(int idVenda) {
        // Primeiro: buscar a venda
        Venda venda = vendaRepository.buscarPorId(idVenda);
        if (venda == null) {
            ultimaMensagem = "❌ Venda não encontrada com ID: " + idVenda;
            return false;  // Falha: venda não encontrada
        }

        // Devolver estoque ao produto (estorno)
        Produto produto = venda.getProduto();
        int novoEstoque = produto.getQtdEstoque() + venda.getQuantidade();
        boolean estoqueAtualizado = produtoRepository.atualizarEstoque(produto.getId(), novoEstoque);

        if (!estoqueAtualizado) {
            ultimaMensagem = "❌ Falha ao devolver estoque.";
            return false;  // Falha: não conseguiu devolver estoque
        }

        // Cancelar venda (exclusão lógica)
        boolean cancelado = vendaRepository.cancelar(idVenda);

        if (cancelado) {
            ultimaMensagem = "✅ Venda cancelada com sucesso!";
        } else {
            ultimaMensagem = vendaRepository.getUltimaMensagem();  // Pega mensagem de erro do repositório
        }

        return cancelado;  // Retorna resultado da operação
    }

    // ================================
    // CALCULAR TOTAL GASTO POR CLIENTE
    // ================================
    public BigDecimal totalGastoPorCliente(int idCliente) {
        // Busca todas as vendas do cliente
        List<Venda> vendasCliente = vendaRepository.buscarPorCliente(idCliente);
        BigDecimal total = BigDecimal.ZERO;  // Inicializa total com zero

        // Soma o valor total de cada venda
        for (Venda v : vendasCliente) {
            total = total.add(v.getValorTotal());
        }

        return total;  // Retorna o total calculado
    }

    // ================================
    // CALCULAR QUANTIDADE VENDIDA DE UM PRODUTO
    // ================================
    public int quantidadeVendidaDoProduto(int idProduto) {
        // Busca todas as vendas do produto
        List<Venda> vendasProduto = vendaRepository.buscarPorProduto(idProduto);
        int total = 0;  // Inicializa total com zero

        // Soma a quantidade de cada venda
        for (Venda v : vendasProduto) {
            total += v.getQuantidade();
        }

        return total;  // Retorna o total calculado
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM
    // ================================
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}