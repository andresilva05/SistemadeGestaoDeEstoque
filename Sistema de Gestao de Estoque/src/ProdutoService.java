import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    private IProdutoRepository repository;

    public ProdutoService(IProdutoRepository repository) {
        this.repository = repository;
    }

    public boolean incluirProduto(int id, String nome, BigDecimal preco, int estoqueInicial) {

        // validar nome
        if (nome == null || nome.isBlank()) {
            return false;
        }

        // nome duplicado
        Produto existente = repository.buscarPorNome(nome);
        if (existente != null) {
            return false;
        }

        // validar preco
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // validar estoque inicial
        if (estoqueInicial < 0) {
            return false;
        }

        // criar produto
        Produto novo = new Produto(id, nome, preco, estoqueInicial, true);
        repository.adicionar(novo);

        return true;
    }

    public boolean excluirProduto(int id) {

        Produto produto = repository.buscarPorId(id);

        if (produto == null) {
            return false;
        }

        if (!produto.isAtivo()) {
            return false;
        }

        // gancho para venda do Gustavo
        boolean produtoFoiVendido = false;

        if (produtoFoiVendido) {
            return false;
        }

        // soft delete
        return repository.excluir(id);
    }

    public Produto buscarProdutoPorId(int id) {
        return repository.buscarPorId(id);
    }

    public void listarProdutos() {
        repository.listar();
    }

    public boolean atualizarEstoque(int id, int novaQuantidade) {
        Produto produto = repository.buscarPorId(id);

        int novaQtd = novaQuantidade;

        if (repository.buscarPorId(id) == null) {
            return false;
        }
        if (!produto.isAtivo()) {
            return false;
        }

        if (novaQtd < 0) {
            return false;

        }
        produto.setQtdEstoque(novaQtd);
        return true;
    }

    public List<Produto> listarEstoqueBaixo() {
        List<Produto> produtos = repository.getAll();
        List<Produto> resultado = new ArrayList<>();

        for (Produto produto : produtos) {
            if (produto.isAtivo() == true && produto.getQtdEstoque() <= 5) {

                resultado.add(produto);

            }
        }
        return resultado;

    }
}
