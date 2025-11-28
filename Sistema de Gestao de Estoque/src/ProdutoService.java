import java.math.BigDecimal;

public class ProdutoService {
    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public boolean incluirProduto(int id, String nome, BigDecimal preco, int estoqueInicial) {

        if (nome == null || nome.isBlank()) {
            return false;
        }

        for (Produto p : repository.produtos) {
            if (p.getNome().equalsIgnoreCase(nome) && p.isAtivo()) {
                return false;
            }
        }

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (estoqueInicial < 0) {
            return false;
        }

        Produto newProduct = new Produto(id, nome, preco, estoqueInicial, true);
        repository.adicionar(newProduct);
        return true;

    }

    public boolean excluirProduto(int id) {

        // PASSO 1 — Buscar produto
        Produto produto = repository.buscarPorId(id);
        if (produto == null) {
            return false;
        }

        // PASSO 2 — Já está inativo?
        if (!produto.isAtivo()) {
            return false;
        }

        // PASSO 3 — Gancho para validação de vendas
        // Quando Gustavo terminar, isso vai chamar o VendaService: boolean foiVendido = vendaService.produtoFoiVendido(id);
        boolean produtoFoiVendido = false; // por enquanto sempre false

        if (produtoFoiVendido) {
            return false;
        }

        // PASSO 4 — Soft delete
        produto.setAtivo(false);

        return true;
    }


    public Produto buscarProdutoPorId(int id) {
        return repository.buscarPorId(id);
    }

    public void listarProdutos() {
        repository.listar();
    }

    public boolean atualizarEstoque(int id, int novaQuantidade) {
        // regra será implementada depois
        return false;
    }

    public void listarEstoqueBaixo() {
        // regra será implementada depois
    }
}
