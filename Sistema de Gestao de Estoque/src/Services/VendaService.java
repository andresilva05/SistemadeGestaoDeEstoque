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

public class VendaService {

    private IVendasRepository vendaRepository;
    private IClienteRepository<ClientePF> clientePFRepository;
    private IClienteRepository<ClientePJ> clientePJRepository;
    private IProdutoRepository produtoRepository;
    private String ultimaMensagem; // Campo adicionado

    public VendaService(IVendasRepository vendaRepository,
                        IClienteRepository<ClientePF> clientePFRepository,
                        IClienteRepository<ClientePJ> clientePJRepository,
                        IProdutoRepository produtoRepository) {

        this.vendaRepository = vendaRepository;
        this.clientePFRepository = clientePFRepository;
        this.clientePJRepository = clientePJRepository;
        this.produtoRepository = produtoRepository;
        this.ultimaMensagem = "";
    }

    public boolean registrarVenda(int idCliente, int idProduto, int quantidade) {

        // Validar quantidade
        if (quantidade <= 0) {
            ultimaMensagem = "❌ Quantidade deve ser positiva.";
            return false;
        }

        // Buscar cliente - tenta primeiro PF, depois PJ
        Cliente cliente = clientePFRepository.buscarPorId(idCliente);
        if (cliente == null) {
            cliente = clientePJRepository.buscarPorId(idCliente);
        }

        if (cliente == null) {
            ultimaMensagem = "❌ Cliente não encontrado com ID: " + idCliente;
            return false;
        }

        // Buscar produto
        Produto produto = produtoRepository.buscarPorId(idProduto);
        if (produto == null) {
            ultimaMensagem = "❌ Produto não encontrado com ID: " + idProduto;
            return false;
        }

        // Validar produto ativo
        if (!produto.isAtivo()) {
            ultimaMensagem = "❌ Produto não está ativo.";
            return false;
        }

        // Validar estoque
        if (produto.getQtdEstoque() < quantidade) {
            ultimaMensagem = "❌ Estoque insuficiente. Disponível: " + produto.getQtdEstoque();
            return false;
        }

        // ATUALIZAR ESTOQUE NO REPOSITÓRIO
        int novoEstoque = produto.getQtdEstoque() - quantidade;
        boolean estoqueAtualizado = produtoRepository.atualizarEstoque(idProduto, novoEstoque);

        if (!estoqueAtualizado) {
            ultimaMensagem = "❌ Falha ao atualizar estoque.";
            return false;
        }

        // Atualizar o objeto produto localmente
        produto.setQtdEstoque(novoEstoque);

        // Criar Venda
        Venda novaVenda = new Venda(0, cliente, produto, quantidade);

        // Registrar venda
        boolean vendaRegistrada = vendaRepository.adicionar(novaVenda);

        if (vendaRegistrada) {
            ultimaMensagem = "✅ Venda registrada com sucesso!";
        } else {
            ultimaMensagem = vendaRepository.getUltimaMensagem();
        }

        return vendaRegistrada;
    }

    public List<Venda> listarVendas() {
        return vendaRepository.getAll();
    }

    public List<Venda> buscarVendasPorCliente(int idCliente) {
        return vendaRepository.buscarPorCliente(idCliente);
    }

    public List<Venda> buscarVendasPorProduto(int idProduto) {
        return vendaRepository.buscarPorProduto(idProduto);
    }

    public boolean cancelarVenda(int idVenda) {

        Venda venda = vendaRepository.buscarPorId(idVenda);
        if (venda == null) {
            ultimaMensagem = "❌ Venda não encontrada com ID: " + idVenda;
            return false;
        }

        // Devolver estoque
        Produto produto = venda.getProduto();
        int novoEstoque = produto.getQtdEstoque() + venda.getQuantidade();
        boolean estoqueAtualizado = produtoRepository.atualizarEstoque(produto.getId(), novoEstoque);

        if (!estoqueAtualizado) {
            ultimaMensagem = "❌ Falha ao devolver estoque.";
            return false;
        }

        // Cancelar venda
        boolean cancelado = vendaRepository.cancelar(idVenda);

        if (cancelado) {
            ultimaMensagem = "✅ Venda cancelada com sucesso!";
        } else {
            ultimaMensagem = vendaRepository.getUltimaMensagem();
        }

        return cancelado;
    }

    public BigDecimal totalGastoPorCliente(int idCliente) {
        List<Venda> vendasCliente = vendaRepository.buscarPorCliente(idCliente);
        BigDecimal total = BigDecimal.ZERO;

        for (Venda v : vendasCliente) {
            total = total.add(v.getValorTotal());
        }

        return total;
    }

    public int quantidadeVendidaDoProduto(int idProduto) {
        List<Venda> vendasProduto = vendaRepository.buscarPorProduto(idProduto);
        int total = 0;

        for (Venda v : vendasProduto) {
            total += v.getQuantidade();
        }

        return total;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}