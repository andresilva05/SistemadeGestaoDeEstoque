package Services;

import Models.Produto;
import Repositories.IProdutoRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoService {
    private IProdutoRepository repository;

    public ProdutoService(IProdutoRepository repository) {
        this.repository = repository;
    }

    public boolean incluirProduto(int id, String nome, BigDecimal preco, int estoqueInicial) {
        // Validar nome
        if (nome == null || nome.isBlank()) {
            return false;
        }

        // Validar preco
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Validar estoque inicial
        if (estoqueInicial < 0) {
            return false;
        }

        // Criar produto
        Produto novo = new Produto(id, nome, preco, estoqueInicial);
        return repository.adicionar(novo);
    }

    public boolean excluirProduto(int id) {
        return repository.excluir(id);
    }

    public Produto buscarProdutoPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Produto> listarProdutos() {
        return repository.getAll();
    }

    public boolean atualizarEstoque(int id, int novaQuantidade) {
        // Validação no service também (segurança dupla)
        if (novaQuantidade < 0) {
            return false;
        }

        Produto produto = repository.buscarPorId(id);
        if (produto == null) {
            return false;
        }

        if (!produto.isAtivo()) {
            return false;
        }

        return repository.atualizarEstoque(id, novaQuantidade);
    }

    // Método para obter mensagem do repositório (se tiver)
    public String getUltimaMensagem() {
        return repository.getUltimaMensagem();
    }
}